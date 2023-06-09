package com.annguyenhoang.annotes.di

import com.annguyenhoang.annotes.data.AuthRepository
import com.annguyenhoang.annotes.data.NoteRepository
import com.annguyenhoang.annotes.data.remote.auth.AuthRepositoryImpl
import com.annguyenhoang.annotes.data.remote.auth.AuthNetworkSource
import com.annguyenhoang.annotes.data.remote.auth.AuthNetworkSourceImpl
import com.annguyenhoang.annotes.data.remote.notes.NoteNetworkSource
import com.annguyenhoang.annotes.data.remote.notes.NoteNetworkSourceImpl
import com.annguyenhoang.annotes.data.remote.notes.NoteRepositoryImpl
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabaseRef(): DatabaseReference = Firebase.database.reference

    @Singleton
    @Provides
    fun provideAuthNetworkSource(databaseRef: DatabaseReference): AuthNetworkSource =
        AuthNetworkSourceImpl(databaseRef)

    @Singleton
    @Provides
    fun provideAuthRepository(authNetworkSource: AuthNetworkSource): AuthRepository =
        AuthRepositoryImpl(authNetworkSource)

    @Singleton
    @Provides
    fun provideNoteNetworkSource(databaseRef: DatabaseReference): NoteNetworkSource =
        NoteNetworkSourceImpl(databaseRef)

    @Singleton
    @Provides
    fun provideNoteRepository(noteNetworkSource: NoteNetworkSource): NoteRepository =
        NoteRepositoryImpl(noteNetworkSource)

}