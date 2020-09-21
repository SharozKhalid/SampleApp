package sharoz.mindgeek.test.ui.main.passcode


import androidx.lifecycle.ViewModel
import sharoz.mindgeek.test.data.EncryptedSharedPrefRepository

class PasscodeViewModel : ViewModel() {

    private var fail: Int = 0
    private var tempPass: String = ""

    fun isPassCodeEnabled(): Boolean? {
        return EncryptedSharedPrefRepository.getInstance().isPassCodeEnabled()
    }

    fun checkPasscode(value: String): Boolean {
        return EncryptedSharedPrefRepository.getInstance().checkPasscode(value) ?: false
    }

    fun addFail() {
        fail++
    }

    fun reachedFails(): Boolean {
        return fail > 2
    }

    fun resetFails() {
        fail = 0
    }

    fun setFirstPass(value: String) {
        tempPass = value
    }

    fun hasFirstPass(): Boolean{
        return tempPass.isNotEmpty()
    }

    fun clearTempPass() {
        tempPass = ""
    }

    fun passMatches(value: String): Boolean {
        return tempPass == value
    }

    fun applyPasscode() {
        EncryptedSharedPrefRepository.getInstance().setPassCode(tempPass)
        clearTempPass()
    }

    fun isAppLocked(): Boolean {
        return EncryptedSharedPrefRepository.getInstance().isAppLocked()
    }

    fun lockApp() {
        EncryptedSharedPrefRepository.getInstance().lockApp()
    }
}