package sharoz.mindgeek.test.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import sharoz.mindgeek.test.Constants.LOCKED
import sharoz.mindgeek.test.Constants.PASSCODE
import sharoz.mindgeek.test.Constants.PASSCODE_PREF
import sharoz.mindgeek.test.Constants.TIME

class EncryptedSharedPrefRepository {

    companion object {

        private lateinit var preferences: SharedPreferences
        private val mInstance: EncryptedSharedPrefRepository by lazy { EncryptedSharedPrefRepository() }

        internal fun init(appContext: Context): EncryptedSharedPrefRepository {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
                val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

                preferences = EncryptedSharedPreferences.create(
                    "shared_preferences_private",
                    masterKeyAlias,
                    appContext,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )


            } else {
                preferences = appContext.getSharedPreferences(PASSCODE_PREF, Context.MODE_PRIVATE)
            }
            return mInstance
        }

        fun getInstance(): EncryptedSharedPrefRepository {
            return mInstance
        }
    }

    fun isPassCodeEnabled(): Boolean? {
        return preferences.getString(PASSCODE, "")?.isNotEmpty()
    }

    fun disablePasscode() {
        preferences.edit().putString(PASSCODE, "").apply()
    }

    fun setPassCode(value: String) {
        preferences.edit().putString(PASSCODE, value).apply()
    }

    fun checkPasscode(value: String): Boolean? {
        return preferences.getString(PASSCODE, "").equals(value)
    }

    fun isAppLocked(): Boolean {
        return preferences.getBoolean(LOCKED, false)
    }

    fun lockApp() {
        preferences.edit().putBoolean(LOCKED, true).apply()
    }

    fun releaseAppLock() {
        preferences.edit().putBoolean(LOCKED, false).apply()
    }

    fun updateTime(value: Long) {
        preferences.edit().putLong(TIME, value).apply()
    }

    fun getTime(): Long {
        return preferences.getLong(TIME, 0)
    }
}