package zykov.andrii.org.androiddispatcher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import zykov.andrii.org.doonce.DoOnce

class MainActivity : AppCompatActivity() {

    private var intervalCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DoOnce.get().let { doOnce ->
            findViewById<Button>(R.id.button).setOnClickListener {
                doOnce.perTimeInterval("INTERVAL", 5_000) {
                    findViewById<TextView>(R.id.per_interval).text = "count: ${++intervalCount}"
                }
            }
        }
    }
}
