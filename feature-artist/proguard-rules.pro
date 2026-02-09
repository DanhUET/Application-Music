# Giữ lại tất cả Dagger/Hilt modules, không để obfuscate
-keep class * {
    @dagger.Module <methods>;
    @dagger.Provides <methods>;
    @dagger.Binds <methods>;
    @dagger.hilt.InstallIn <methods>;
}
-keep class **.*RepositoryImpl { *; }
-keep class **.*UseCase { *; }

-keep class pro.branium.feature_artist.data.dao.** { *; }
-keep class pro.branium.feature_artist.data.dao.** { *; }
-keep class pro.branium.feature_artist.data.entity.** { *; }
-keep class pro.branium.feature_artist.data.dto.** { *; }
-keep class pro.branium.feature_artist.data.datasource.** { *; }
-keep class pro.branium.feature_artist.domain.repository.** { *; }
-keep class pro.branium.infrastructure.repository.artist.** { *; }
-keep class  pro.branium.feature_artist.data.api.** { *; }

