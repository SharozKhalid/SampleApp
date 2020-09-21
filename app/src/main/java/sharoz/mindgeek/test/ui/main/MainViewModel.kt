package sharoz.mindgeek.test.ui.main

import androidx.lifecycle.ViewModel
import sharoz.mindgeek.test.data.EncryptedSharedPrefRepository

class MainViewModel : ViewModel() {

    fun isPassCodeEnabled(): Boolean? {
        return EncryptedSharedPrefRepository.getInstance().isPassCodeEnabled()
    }
}