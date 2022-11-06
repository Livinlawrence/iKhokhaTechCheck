package com.ikhokha.techcheck.data.remote.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ikhokha.techcheck.data.local.ProductDao
import com.ikhokha.techcheck.data.local.model.asUiModel
import com.ikhokha.techcheck.data.mappers.toLoggedInUserMap
import com.ikhokha.techcheck.data.mappers.toProductMap
import com.ikhokha.techcheck.data.remote.BusyShopNetworkDataSource
import com.ikhokha.techcheck.data.remote.dto.ProductDto
import com.ikhokha.techcheck.data.remote.dto.ValueDto
import com.ikhokha.techcheck.domain.model.LoggedInUser
import com.ikhokha.techcheck.domain.model.Product
import com.ikhokha.techcheck.domain.util.Resource
import com.ikhokha.techcheck.domain.util.performFirebaseTransaction
import com.livin.ikhokhatechcheck.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseBusyShopNetwork @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val productDao: ProductDao
) : BusyShopNetworkDataSource {

    override suspend fun login(email: String, password: String): Resource<LoggedInUser> {
        lateinit var result: Resource<LoggedInUser>
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            result = if (task.isSuccessful) {
                //This commented code is for public login feature
                //If this was not internal user then could make use of this feature for actual user login
                firebaseAuth.currentUser?.let {
                    result = Resource.Success(it.toLoggedInUserMap())
                }?.run {
                    result = Resource.Error("Something went wrong")
                }
                Resource.Success(LoggedInUser(email))
            } else {
                Resource.Error(task.exception?.localizedMessage ?: "Something went wrong")
            }
        }.addOnFailureListener {
            result = Resource.Error(it.localizedMessage ?: "Something went wrong")
        }.await()
        return result
    }

    override suspend fun getProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            performFirebaseTransaction {
                val firebaseData = firebaseDatabase.reference.get().await()
                val products = firebaseData.children.mapNotNull { doc ->
                    val valueDto = doc.getValue(ValueDto::class.java)
                    val downloadUrl =
                        Firebase.storage.getReferenceFromUrl(BuildConfig.IMAGE_URL_END_POINT + "" + valueDto?.image).downloadUrl.await()

                    ProductDto(
                        key = doc.key, value = valueDto, imageUrl = downloadUrl.toString()
                    ).toProductMap()
                }
                Log.e("products", products.size.toString() + "")
                productDao.insertProduct(products)
                products.map { it.asUiModel() }
            }
        }
    }
}