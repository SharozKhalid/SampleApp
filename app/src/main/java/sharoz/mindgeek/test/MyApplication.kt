package sharoz.mindgeek.test

import android.app.Application
import sharoz.mindgeek.test.data.EncryptedSharedPrefRepository

class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        EncryptedSharedPrefRepository.init(applicationContext)
    }
}