package com.example.saloonservice.ui.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.saloonservice.MainActivity
import com.example.saloonservice.R
import com.example.saloonservice.databinding.ActivitySplashBinding
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Splash Text Transition
        val animation = AnimationUtils.loadAnimation(this, R.anim.side_slide);
        binding.lytAppName.startAnimation(animation)

        // Scheduled Executor to launch homescreen
        val splashExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
        splashExecutor.schedule({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },3, TimeUnit.SECONDS) // Execute after 3 Seconds
    }


}