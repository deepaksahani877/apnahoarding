
package com.app.apnahoarding.core.repository


import com.app.apnahoarding.core.models.WallData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WallRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun getAllWalls(): List<WallData> {
        val snapshot = db.collection("walls_db")
            .get()
            .await()  // Suspend-friendly, no blocking

        return snapshot.toObjects(WallData::class.java)
    }


    suspend fun getFeaturedWalls(limit:Long = 5): List<WallData> {
        val snapshot = db.collection("walls_db")
            .whereEqualTo("isFeatured", true)
            .get()
            .await()  // Suspend-friendly, no blocking

        return snapshot.toObjects(WallData::class.java)
    }


    suspend fun getRecentWalls(limit: Long = 5): List<WallData> {
        val snapshot = db.collection("walls_db")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()

        return snapshot.toObjects(WallData::class.java)
    }



    suspend fun getPaginatedWalls(startAfter: String? = null, limit: Long = 10): List<WallData> {
        var query = db.collection("walls_db")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit)

        if (!startAfter.isNullOrEmpty()) {
            val snapshot = db.collection("walls_db")
                .whereEqualTo("uploaderUid", startAfter)
                .limit(1)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                query = query.startAfter(snapshot.documents.first())
            }
        }

        val snapshot = query.get().await()
        return snapshot.toObjects(WallData::class.java)
    }


}
