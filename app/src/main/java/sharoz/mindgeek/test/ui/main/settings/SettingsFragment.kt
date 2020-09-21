package sharoz.mindgeek.test.ui.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.settings_fragment.*
import sharoz.mindgeek.test.Constants
import sharoz.mindgeek.test.R
import sharoz.mindgeek.test.ui.main.MainActivity


class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        sw_passcode.isChecked = viewModel.isPassCodeEnabled()?: false

        sw_passcode.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                (activity as MainActivity).showPassCodeFragment(Constants.NEW_PASSCODE)
            }else{
                viewModel.disablePasscode()
            }
        }
    }
}