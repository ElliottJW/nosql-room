# NoSql + Room

[![CI](https://github.com/ElliottJW/nosql-room/actions/workflows/ci.yml/badge.svg)](https://github.com/ElliottJW/nosql-room/actions/workflows/ci.yml)

A simple NoSql library built on top of Android's Room DB.

Please report an Issue if you encounter one. 

## Installation

NoSql-Room is currently deployed automatically via GitHub packages. Next step will be to get it on MavenCentral for easier access.

In the mean time, in your `settings.gradle`, add a `maven` block with your GitHub credentials:

```groovy
dependencyResolutionManagement {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ElliottJW/nosql-room")
            credentials {
                username = '<Your GH username / email>'
                password = '<Your personal access token with `read:packages` permission>'
            }
        }
    }
}
```

## Usage

Get an instance of the `DatabaseClient`:

```kotlin
    val client = NoSqlRoomClient.getDatabase(context = context)
```

It can be easily used via dependency injection. Using DI helps guarantee you're using only one instance of the DB. Here's an example with Hilt:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object SampleAppModule {
    
    @Provides
    fun provideDatabaseClient(
        @ApplicationContext context: Context
    ) : DatabaseClient {
        return NoSqlRoomClient.getDatabase(context = context)
    }
    
    // ... Other dependencies ...
}
```

The [`DatabaseClient`](./nosql-room/src/main/java/dev/libatorium/nosqlroom/client/DatabaseClient.kt) interface implements one-shot methods for deleting, saving, and obtaining the count of items saved. The user can also collect changes via kotlin Flows for a specific item, or multiple items.