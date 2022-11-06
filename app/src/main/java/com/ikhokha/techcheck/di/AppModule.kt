package com.ikhokha.techcheck.di


import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ikhokha.techcheck.data.local.CartDao
import com.ikhokha.techcheck.data.local.OrderDao
import com.ikhokha.techcheck.data.local.ProductDao
import com.ikhokha.techcheck.data.local.ShoppingCartDatabase
import com.livin.ikhokhatechcheck.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database(BuildConfig.FIREBASE_DB_END_POINT)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return  FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): ShoppingCartDatabase {
        return Room.databaseBuilder(
            app,
            ShoppingCartDatabase::class.java,
            ShoppingCartDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(shoppingCartDatabase: ShoppingCartDatabase): ProductDao {
        return shoppingCartDatabase.productDao
    }

    @Provides
    @Singleton
    fun provideShoppingCartDao(shoppingCartDatabase: ShoppingCartDatabase): CartDao {
        return shoppingCartDatabase.cartDao
    }

    @Provides
    @Singleton
    fun provideOrderDao(shoppingCartDatabase: ShoppingCartDatabase): OrderDao {
        return shoppingCartDatabase.orderDao
    }
}