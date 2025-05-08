package com.app.apnahoarding.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.app.apnahoarding.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID

class AuthenticationManager(val context: Context) {

    private val auth = Firebase.auth


    private fun createNonce():String{
        val rawNonce = UUID.randomUUID().toString()
        val byte = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(byte)
        return digest.fold(""){
                str,it->str+"%02x".format(it)
        }   
    }

    fun signInWithGoogle(): Flow<AuthResponse> = callbackFlow {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()


        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                context = context, request = request
            )

            val credential = result.credential

            if(credential is CustomCredential){
                if(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    try{

                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken,null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener {

                                if(it.isSuccessful){
                                    val user = auth.currentUser
                                    val email = user?.email ?: ""
                                    val name = user?.displayName
                                    val photoUrl = user?.photoUrl?.toString()
                                    trySend(AuthResponse.Success(email, name, photoUrl))
                                }
                                else{
                                    trySend(AuthResponse.Error(message = it.exception?.message?:""))
                                }

                            }
                    }catch (e: GoogleIdTokenParsingException){
                        trySend(AuthResponse.Error(e.message?:""))
                    }
                }
            }

        }catch (e: Exception){
            trySend(AuthResponse.Error(e.message?:""))
        }

        awaitClose()


    }
}


interface AuthResponse {
    data class Success( val email: String,
                        val name: String?,
                        val photoUrl: String?) : AuthResponse;
    data class Error(val message: String) : AuthResponse
}