package dev.libatorium.nosqlroom.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.libatorium.nosqlroom.NoSqlRoomClient
import dev.libatorium.nosqlroom.client.DatabaseClient
import dev.libatorium.nosqlroom.data.provider.UserIdProviderImpl
import dev.libatorium.nosqlroom.data.repository.UserRepositoryImpl
import dev.libatorium.nosqlroom.domain.provider.UserIdProvider
import dev.libatorium.nosqlroom.domain.repository.UserRepository

@Module
@InstallIn(SingletonComponent::class)
object SampleAppModule {

    const val SHARED_PREFS_KEY = "nosql-room-sample-shared-prefs"

    @Provides
    fun provideUserIdProvider(
        @ApplicationContext context: Context
    ) : UserIdProvider {
        val prefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        return UserIdProviderImpl(
            sharedPreferences = prefs
        )
    }

    @Provides
    fun provideDatabaseClient(
        @ApplicationContext context: Context
    ) : DatabaseClient {
       return NoSqlRoomClient.getDatabase(context = context)
    }

    @Provides
    fun provideUserRepository(
        userIdProvider: UserIdProvider,
        databaseClient: DatabaseClient
    ) : UserRepository {
        return UserRepositoryImpl(
            databaseClient = databaseClient,
            userIdProvider = userIdProvider
        )
    }
}