package zykov.andrii.org.doonceexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import zykov.andrii.org.doonce.DoOnce
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var intervalCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DoOnce.get().let { doOnce ->
            findViewById<Button>(R.id.button).setOnClickListener {
                TimeUnit.MILLISECONDS
                doOnce.perTimeInterval("INTERVAL", 5_000) {
                    findViewById<TextView>(R.id.per_interval).text = "count: ${++intervalCount}"
                }
            }
        }
    }
}
