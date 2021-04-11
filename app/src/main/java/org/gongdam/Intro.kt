package org.gongdam

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import kotlinx.android.synthetic.main.intro.*
import org.jetbrains.anko.toast

class Intro :AppCompatActivity(){

    val REQUEST_CODE_UPDATE = 205
    lateinit var appUpdateManager : AppUpdateManager
    lateinit var appUpdateInfoTask: Task<AppUpdateInfo>
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_UPDATE){
            if(resultCode != Activity.RESULT_OK){
                toast("업데이트가 취소 되었습니다.")
                finishAffinity()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if(it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                appUpdateManager.startUpdateFlowForResult(
                    it, AppUpdateType.IMMEDIATE, this, REQUEST_CODE_UPDATE
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)
        //화면 회전 없앰
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateInfoTask = appUpdateManager.appUpdateInfo


        appUpdateManager?.let{
            it.appUpdateInfo.addOnSuccessListener {appUpdateInfo ->
                if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo, AppUpdateType.IMMEDIATE, this, REQUEST_CODE_UPDATE
                    )
                }
            }
        }

        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        intro_page_imageView.startAnimation(fadeInAnim)

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)

    }

}