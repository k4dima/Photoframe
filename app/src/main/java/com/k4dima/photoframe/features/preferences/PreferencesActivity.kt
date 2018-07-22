package com.k4dima.photoframe.features.preferences

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import com.k4dima.photoframe.R

class PreferencesActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        supportFragmentManager.transaction {
            add(R.id.content, PreferencesFragment())
        }
    }
}