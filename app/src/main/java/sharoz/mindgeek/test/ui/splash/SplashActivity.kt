package sharoz.mindgeek.test.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import sharoz.mindgeek.test.R
import sharoz.mindgeek.test.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        viewModel.finished.observe(this, Observer {

            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        viewModel.startTimer()
    }
}


