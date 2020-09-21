package sharoz.mindgeek.test.ui.splash

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {

    private lateinit var timer: CountDownTimer

    val finished = MutableLiveData<Boolean>()

    fun startTimer() {

        timer = object : CountDownTimer(2000, 2000) {

            override fun onFinish() {
                finished.value = true
            }

            override fun onTick(p0: Long) {

            }
        }.start()

    }

    fun cancelTimer() {
        timer.cancel()
    }

}