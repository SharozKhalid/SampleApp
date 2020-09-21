package sharoz.mindgeek.test.ui.main.settings

import androidx.lifecycle.ViewModel
import sharoz.mindgeek.test.data.EncryptedSharedPrefRepository

class SettingsViewModel : ViewModel() {

    fun isPassCodeEnabled(): Boolean? {
        return EncryptedSharedPrefRepository.getInstance().isPassCodeEnabled()
    }

    fun disablePasscode() {
        EncryptedSharedPrefRepository.getInstance().disablePasscode()
    }
}