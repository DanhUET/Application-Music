# =======================================
# Consumer rules cho module :feature-favorite
# =======================================
# Giữ lại tất cả Dagger/Hilt modules, không để obfuscate
-keep class * {
    @dagger.Module <methods>;
    @dagger.Provides <methods>;
    @dagger.Binds <methods>;
    @dagger.hilt.InstallIn <methods>;
}

-keep class pro.branium.feature_favorite.data.entity.** { *; }
-keep class pro.branium.feature_favorite.data.dao.** { *; }

-keep class pro.branium.feature_favorite.data.dao.**Dao_Impl { *; }
-keep class pro.branium.feature_favorite.data.datasource.** { *; }
-keep class pro.branium.infrastructure.repository.favorite.** { *; }
-keep class pro.branium.feature_favorite.ui.menu.** { *; }
