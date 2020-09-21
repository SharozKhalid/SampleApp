package sharoz.mindgeek.test.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import sharoz.mindgeek.test.service.BroadcastService
import sharoz.mindgeek.test.Constants
import sharoz.mindgeek.test.Constants.COUNTDOWN
import sharoz.mindgeek.test.Constants.OPTION
import sharoz.mindgeek.test.R
import sharoz.mindgeek.test.ui.main.passcode.PasscodeFragment
import sharoz.mindgeek.test.ui.main.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var passcodeFragment: PasscodeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        if (savedInstanceState == null) {

            if (viewModel.isPassCodeEnabled() == true) {
                showPassCodeFragment(Constants.ENTER_PASSCODE)
            } else {
                showSettingsFragment()
            }
        }
    }

    fun showSettingsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SettingsFragment.newInstance())
            .commitNow()
    }

    fun showPassCodeFragment(option: Int) {

        passcodeFragment = PasscodeFragment.newInstance()
        val bundle = Bundle()
        bundle.putInt(OPTION, option)
        passcodeFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, passcodeFragment)
            .commitNow()
    }

    private val br: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateGUI(intent)
        }
    }

    private fun updateGUI(intent: Intent) {
        if (intent.extras != null) {
            val seconds = intent.getLongExtra(COUNTDOWN, 0)
            if (seconds == 0L) {
                passcodeFragment.unlockState()
            }
            passcodeFragment.setLockedText(getString(R.string.too_many_attempts) + " " + seconds + " second(s).")
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isPassCodeEnabled() == true) {
            showPassCodeFragment(Constants.ENTER_PASSCODE)
        } else {
            showSettingsFragment()
        }
        registerReceiver(br, IntentFilter(BroadcastService.TIMER_BR))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(br)
    }

    override fun onStop() {
        try {
            unregisterReceiver(br)
        } catch (e: Exception) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop()
    }
}