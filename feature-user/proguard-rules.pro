# Giữ lại tất cả Dagger/Hilt modules, không để obfuscate
-keep class * {
    @dagger.Module <methods>;
    @dagger.Provides <methods>;
    @dagger.Binds <methods>;
    @dagger.hilt.InstallIn <methods>;
}
-keep class **.*RepositoryImpl { *; }
-keep class **.*UseCase { *; }

-keep class pro.branium.feature_user.data.datasource.** { *; }
-keep class pro.branium.feature_user.domain.repository.** { *; }
-keep class pro.branium.infrastructure.repository.user.** { *; }
-keep class pro.branium.feature_user.domain.model.** { *; }
