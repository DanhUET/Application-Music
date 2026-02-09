# Giữ lại tất cả Dagger/Hilt modules, không để obfuscate
-keep class * {
    @dagger.Module <methods>;
    @dagger.Provides <methods>;
    @dagger.Binds <methods>;
    @dagger.hilt.InstallIn <methods>;
}

-keep class pro.branium.feature_song.data.** { *; }
-keep class pro.branium.feature_song.domain.usecase.** { *; }
# ActionHandler
-keep class pro.branium.feature_song.action.** { *; }
-keep class pro.branium.feature_song.action.handler.** { *; }
-keep class pro.branium.feature_song.domain.repository.** { *; }

# MenuProvider
-keep class pro.branium.feature_song.ui.menu.provider.** { *; }

# Dialogs / Fragments (Navigation sử dụng reflection)
-keep class pro.branium.feature_song.ui.dialogs.** { *; }
