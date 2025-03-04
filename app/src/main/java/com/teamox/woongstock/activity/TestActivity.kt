package com.teamox.woongstock.activity

import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.teamox.woongstock.R
import com.teamox.woongstock.databinding.ActivityInventoryListBinding

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inventory_list)

        val aAppPackage = "com.media.music.mp3.musicplayer" // B 어플 패키지 이름
        val bAppPackage = "com.samsung.android.app.notes" // C 어플 패키지 이름
        val cAppPackage = "com.sec.android.gallery3d" // C 어플 패키지 이름
        val dAppPackage = "com.sec.android.app.sbrowser" // C 어플 패키지 이름

//        val button = findViewById<AppCompatButton>(R.id.test_btn)
//        button.setOnClickListener { openAppInSplitScreenMode(aAppPackage) }
//        val button2 = findViewById<AppCompatButton>(R.id.test_btn2)
//        button2.setOnClickListener { openAppInSplitScreenMode(bAppPackage) }
//        val button3 = findViewById<AppCompatButton>(R.id.test_btn3)
//        button3.setOnClickListener { openAppInSplitScreenMode(cAppPackage) }
//        val button4 = findViewById<AppCompatButton>(R.id.test_btn4)
//        button4.setOnClickListener { openAppInSplitScreenMode(dAppPackage) }


        // 각각의 앱을 실행
//        openAppInSplitScreenMode(aAppPackage)
//        openAppInSplitScreenMode(cAppPackage)
//        finish()
//        launchBAppInSplitScreen("com.sec.android.app.popupcalculator", "com.sec.android.gallery3d")
    }

    private fun openAppInSplitScreenMode(packageName: String) {
        val pm: PackageManager = packageManager
        try {
            // 앱을 실행할 인텐트 생성
            val intent: Intent? = pm.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                // 분할 화면 모드에 필요한 플래그 설정
                intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                // 해당 앱이 설치되어 있지 않을 때
                Toast.makeText(this, "$packageName 앱이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "앱을 찾을 수 없습니다: $packageName", Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchBAppInSplitScreen(packageName: String,packageNameSecond: String) {
        val pm: PackageManager = packageManager
        try {
            // B 어플을 실행할 인텐트 생성
            val intent: Intent? = pm.getLaunchIntentForPackage(packageName)
            val intentSecond: Intent? = pm.getLaunchIntentForPackage(packageNameSecond)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK)
                intentSecond!!.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intentSecond)
                startActivity(intent)
                finish()


                // A 어플을 분할 화면 모드로 전환하려면 이 호출을 수행합니다.
//                enterSplitScreenMode()
            } else {
                // B 어플이 설치되지 않은 경우
                Toast.makeText(this, "B 어플이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "B 어플을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun enterSplitScreenMode() {
//        // A 어플을 분할 화면 모드로 전환하는 메서드
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
//            if (activityManager.isInMultiWindowMode) {
//                // 이미 멀티 윈도우 모드에 있을 때
//                Toast.makeText(this, "이미 분할 화면 모드입니다.", Toast.LENGTH_SHORT).show()
//            } else {
//                // 분할 화면 모드로 전환
//                try {
//                    startActivity(
//                        Intent(this, MainActivity::class.java)
//                            .addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK)
//                    )
//                } catch (e: Exception) {
//                    Toast.makeText(this, "분할 화면 모드로 전환할 수 없습니다.", Toast.LENGTH_SHORT).show()
//                    e.printStackTrace()
//                }
//            }
//        } else {
//            Toast.makeText(this, "Android 7.0 (Nougat) 이상에서만 지원됩니다.", Toast.LENGTH_SHORT).show()
//        }
//    }

}