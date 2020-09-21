package sharoz.mindgeek.test.ui.main.passcode


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.passcode_fragment.*
import sharoz.mindgeek.test.service.BroadcastService
import sharoz.mindgeek.test.Constants.ENTER_PASSCODE
import sharoz.mindgeek.test.Constants.NEW_PASSCODE
import sharoz.mindgeek.test.Constants.OPTION
import sharoz.mindgeek.test.R
import sharoz.mindgeek.test.ui.main.MainActivity
import java.util.*

class PasscodeFragment : Fragment() {

    companion object {
        fun newInstance() = PasscodeFragment()
    }

    private lateinit var viewModel: PasscodeViewModel
    private lateinit var circles: ArrayList<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.passcode_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PasscodeViewModel::class.java)

        circles = ArrayList<View>()
        circles.add(circle1)
        circles.add(circle2)
        circles.add(circle3)
        circles.add(circle4)

        et_passcode.clearFocus()
        et_passcode.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                fillCircles(s.length)

                if (viewModel.isPassCodeEnabled() == true) {

                    if (s.length == 4 && viewModel.checkPasscode(s.toString())) {

                        toggleKeyboard(false)
                        (activity as MainActivity).showSettingsFragment()

                    } else if (s.length >= 4) {

                        et_passcode.setText("")
                        tv_incorrect.visibility = View.VISIBLE

                        viewModel.addFail()

                        if (viewModel.reachedFails()) {
                            tv_locked.visibility = View.VISIBLE
                            et_passcode.isEnabled = false
                            viewModel.resetFails()

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                activity?.startForegroundService(Intent(activity, BroadcastService::class.java))
                            }else {
                                activity?.startService(Intent(activity, BroadcastService::class.java))
                            }

                            viewModel.lockApp()
                        }
                    }
                } else {

                    if (s.length == 4 && !viewModel.hasFirstPass()) {

                        viewModel.setFirstPass(s.toString())
                        et_passcode.setText("")
                        tv_passcode.setText(R.string.confirm_passcode)

                    } else if (s.length == 4) {

                        if (viewModel.passMatches(s.toString())) {

                            viewModel.applyPasscode()
                            toggleKeyboard(false)
                            (activity as MainActivity).showSettingsFragment()

                        } else {
                            et_passcode.setText("")
                            tv_incorrect.visibility = View.VISIBLE
                        }

                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {}
        })

        if (arguments != null) {
            when (arguments!!.getInt(OPTION)) {
                ENTER_PASSCODE -> setEnterPasscode()
                NEW_PASSCODE -> setNewPasscode()
            }
        }

    }

    private fun setEnterPasscode() {

        et_passcode.setText("")
        tv_passcode.setText(R.string.enter_passcode)
        tv_incorrect.visibility = View.GONE

        if (viewModel.isAppLocked()) {
            tv_locked.visibility = View.VISIBLE
            et_passcode.isEnabled = false
        } else {
            tv_locked.visibility = View.GONE
            toggleKeyboard(true)
        }
    }

    private fun setNewPasscode() {

        viewModel.clearTempPass()
        et_passcode.setText("")
        tv_passcode.setText(R.string.new_passcode)
        tv_incorrect.visibility = View.GONE
        tv_locked.visibility = View.GONE
        toggleKeyboard(true)
    }

    fun fillCircles(amount: Int) {
        if (amount > 4) {
            return
        }
        for (i in circles.indices) {
            if (i < amount) {
                circles[i].background = activity?.getDrawable(R.drawable.circle_filled)
            } else {
                circles[i].background = activity?.getDrawable(R.drawable.circle)
            }
        }
    }

    fun toggleKeyboard(show: Boolean) {
        et_passcode.requestFocus()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show) {
            et_passcode.postDelayed(Runnable {
                imm.showSoftInput(
                    et_passcode,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }, 500)
        } else imm.hideSoftInputFromWindow(et_passcode.windowToken, 0)
    }

    fun unlockState() {
        tv_incorrect.visibility = View.GONE
        tv_locked.visibility = View.GONE
        et_passcode.isEnabled = true
        toggleKeyboard(true)
    }

    fun setLockedText(lockedText: String?) {
        tv_locked.text = lockedText
    }
}