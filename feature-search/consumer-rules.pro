# =======================================
# Consumer rules cho module :feature-search
# =======================================
# Giữ lại tất cả Dagger/Hilt modules, không để obfuscate
-keep class * {
    @dagger.Module <methods>;
    @dagger.Provides <methods>;
    @dagger.Binds <methods>;
    @dagger.hilt.InstallIn <methods>;
}

-keep class pro.branium.feature.search.data.entity.** { *; }
-keep class pro.branium.feature.search.data.dao.** { *; }
-keep class pro.branium.feature.search.domain.model.** { *; }

-keep class pro.branium.feature.search.data.dao.**Dao_Impl { *; }
-keep class pro.branium.feature.search.data.datasource.** { *; }
-keep class pro.branium.feature.search.domain.repository.** { *; }
-keep class pro.branium.infrastructure.repository.searching.** { *; }
