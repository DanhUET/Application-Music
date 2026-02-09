# Giữ lại tất cả Dagger/Hilt modules, không để obfuscate
-keep class * {
    @dagger.Module <methods>;
    @dagger.Provides <methods>;
    @dagger.Binds <methods>;
    @dagger.hilt.InstallIn <methods>;
}