package sharoz.mindgeek.test.service


import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import sharoz.mindgeek.test.R
import sharoz.mindgeek.test.data.EncryptedSharedPrefRepository

class BroadcastService : Service() {

    companion object {
        const val TIMER_BR = "sharoz.mindgeek.timer_br"
    }

    private var bi = Intent(TIMER_BR)
    private lateinit var cdt: CountDownTimer
    private lateinit var rep: EncryptedSharedPrefRepository

    private val foregroundNotificationId: Int = (System.currentTimeMillis() % 10000).toInt()

    private val foregroundNotification by lazy {
        NotificationCompat.Builder(this, foregroundNotificationChannelId)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setSound(null)
            .build()
    }

    private val foregroundNotificationChannelName by lazy {
        getString(R.string.service_name)
    }

    private val foregroundNotificationChannelDescription by lazy {
        getString(R.string.service_description)
    }

    private val foregroundNotificationChannelId by lazy {
        "ForegroundService.NotificationChannel".also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                    if (getNotificationChannel(it) == null) {
                        createNotificationChannel(
                            NotificationChannel(
                                it,
                                foregroundNotificationChannelName,
                                NotificationManager.IMPORTANCE_MIN
                            ).also {
                                it.description = foregroundNotificationChannelDescription
                                it.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
                                it.vibrationPattern = null
                                it.setSound(null, null)
                                it.setShowBadge(false)
                            })
                    }
                }
            }
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(foregroundNotificationId, foregroundNotification)
            doCountDownTask()
        }
        return START_STICKY;
    }

    private fun doCountDownTask() {

        rep = EncryptedSharedPrefRepository.getInstance()

        var time: Long = 60
        if (rep.getTime() > 0) {
            time = rep.getTime()
        }

        cdt = object : CountDownTimer(time * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                if (seconds > 0) {
                    bi.putExtra("countdown", seconds)
                    sendBroadcast(bi)
                    rep.updateTime(seconds)
                    Log.i("TIMER", "time: " + millisUntilFinished / 1000)
                }
            }

            override fun onFinish() {
                bi.putExtra("countdown", 0)
                sendBroadcast(bi)
                rep.updateTime(0)
                rep.releaseAppLock()
                this@BroadcastService.stopSelf()
            }
        }
        cdt.start()
    }

    override fun onDestroy() {
        cdt.cancel()
        super.onDestroy()
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }
}