package pro.branium.musicapp

import android.app.Application
import android.app.UiModeManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import androidx.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pro.branium.core_utils.MusicAppUtils
import pro.branium.feature_user.domain.model.UserModel
import pro.branium.feature_user.domain.usecase.RegisterUseCase
import pro.branium.feature_user.ui.SettingsFragment
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class MusicApplication : Application() {
    @Inject
    lateinit var registerUseCase: RegisterUseCase

    override fun onCreate() {
        super.onCreate()
        setupDefaultUser()
        setupLanguage()
        setupNightMode()
    }

    private fun setupDefaultUser() {
        CoroutineScope(Dispatchers.IO).launch {
            val defaultUserModel = UserModel(userId = 0, username = "default", password = "")
            val isSuccess = registerUseCase(defaultUserModel)
            if (isSuccess) {
                val sharedPref = getSharedPreferences(MusicAppUtils.PREF_FILE_NAME, MODE_PRIVATE)
                sharedPref.edit {
                    putInt(MusicAppUtils.PREF_CURRENT_USER_ID, defaultUserModel.userId)
                }
            }
        }
    }

    private fun setupLanguage() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val defaultLanguage = Locale.getDefault().language
        val language =
            sharedPref.getString(SettingsFragment.Companion.KEY_PREF_LANGUAGE, defaultLanguage)
                ?: defaultLanguage
        val localeListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(localeListCompat)
        sharedPref.edit {
            putString(SettingsFragment.Companion.KEY_PREF_LANGUAGE, language)
        }
    }

    private fun setupNightMode() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val nightMode = sharedPref.getBoolean(SettingsFragment.Companion.KEY_PREF_DARK_MODE, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val modeUi = if (nightMode) {
                UiModeManager.MODE_NIGHT_YES
            } else {
                UiModeManager.MODE_NIGHT_NO
            }
            val uiModeManager = applicationContext.getSystemService(UiModeManager::class.java)
            uiModeManager.setApplicationNightMode(modeUi)
        } else {
            val modeUi = if (nightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(modeUi)
        }
    }
}