# Giữ lại tất cả Dagger/Hilt modules, không để obfuscate
-keep class * {
    @dagger.Module <methods>;
    @dagger.Provides <methods>;
    @dagger.Binds <methods>;
    @dagger.hilt.InstallIn <methods>;
}

-keep class pro.branium.feature_playlist.data.datasource.** { *; }
-keep class pro.branium.feature_playlist.domain.repository.** { *; }
-keep class pro.branium.infrastructure.repository.playlist.** { *; }