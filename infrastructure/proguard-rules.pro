# =====================================
# ProGuard rules for module :infrastructure
# =====================================

####################################
# Hilt / Dagger / Injection
####################################

# Giữ lại tất cả class được đánh dấu @Module, @InstallIn, @Provides, @Binds
-keep class * {
    @dagger.Module <methods>;
    @dagger.Provides <methods>;
    @dagger.Binds <methods>;
    @dagger.hilt.InstallIn <methods>;
}
-keep interface pro.branium.infrastructure.repository.** { *; }
-keep class **.*RepositoryImpl { *; }
-keep class **.*UseCase { *; }

# Giữ lại Hilt generated classes
-keep class dagger.hilt.internal.generated.** { *; }
-keep class hilt_aggregated_deps.** { *; }

# Giữ các class repository implement để không bị xoá
-keep class pro.branium.infrastructure.permission.** { *; }
-keep class pro.branium.infrastructure.playback.** { *; }

# Keep AppDatabase and generated Impl
-keep class pro.branium.infrastructure.db.AppDatabase { *; }
-keep class pro.branium.infrastructure.db.AppDatabase_Impl { *; }

# Keep any RoomDatabase subclasses
-keep class * extends androidx.room.RoomDatabase { *; }

# Keep generated Dao_Impl inside this module (if any)
-keep class pro.branium.infrastructure.db.**Dao_Impl { *; }

# Giữ lại PlaybackService
-keep class pro.branium.infrastructure.media.PlaybackService { *; }

# Giữ lại Media3 Service
-keep class androidx.media3.session.MediaSessionService { *; }
-keep class androidx.media3.session.MediaLibraryService { *; }

-keep class pro.branium.infrastructure.playback.** { *; }
-keep class pro.branium.infrastructure.di.playback.** { *; }
-keep class pro.branium.infrastructure.di.permission.** { *; }

# Repository implementation
-keep class pro.branium.infrastructure.repository.** { *; }

# DataSource implementation
-keep class pro.branium.infrastructure.source.** { *; }
-keep class **.*DataSourceImpl { *; }

# Mapper
-keep class pro.branium.infrastructure.mapper.** { *; }
-keep class **.*MapperKt { *; }

# DI Modules
-keep class pro.branium.infrastructure.di.** { *; }

# Media / Service
-keep class pro.branium.infrastructure.media.PlaybackService { *; }
-keep class androidx.media3.session.MediaSessionService { *; }
-keep class androidx.media3.session.MediaLibraryService { *; }

-dontwarn pro.branium.infrastructure.db.**
