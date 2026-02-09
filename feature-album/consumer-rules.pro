# Giữ lại tất cả Dagger/Hilt modules, không để obfuscate
-keep class * {
    @dagger.Module <methods>;
    @dagger.Provides <methods>;
    @dagger.Binds <methods>;
    @dagger.hilt.InstallIn <methods>;
}

# DTO
-keep class pro.branium.feature_album.data.dto.** { *; }

# DataSource + Repository
-keep class pro.branium.feature_album.data.datasource.** { *; }
-keep class pro.branium.feature_album.domain.repository.** { *; }
-keep class pro.branium.infrastructure.repository.album.** { *; }

# Model
-keep class pro.branium.feature_album.domain.model.** { *; }

# ViewModel
-keepclassmembers class pro.branium.feature_album.viewmodel.** extends androidx.lifecycle.ViewModel {
    <init>(...);
}