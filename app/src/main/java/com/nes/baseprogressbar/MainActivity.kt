package com.nes.baseprogressbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var progress = 0.0F

        Thread(Runnable {
            while (progress <= progressbar.max){
                progress += 1F

                Thread.sleep(20)

                runOnUiThread {
                    progressbar.progress = progress
                }

                if(progress == progressbar.max){
                    progress = 0.0F
                }
            }
        }).start()
    }
}
