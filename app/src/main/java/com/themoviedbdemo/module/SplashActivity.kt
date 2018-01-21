package com.themoviedbdemo.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import java.util.concurrent.TimeUnit
import io.reactivex.Observable
import com.themoviedbdemo.R
import com.themoviedbdemo.module.home.view.HomeActivity

/**
 * Created by Saveen on 21/01/18.
 * initial screen for application to present app logo to users
 */

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Observable.timer(3, TimeUnit.SECONDS).subscribe { aLong ->

           startActivity(HomeActivity.createIntent(this@SplashActivity))
           finish()
        }
    }

}