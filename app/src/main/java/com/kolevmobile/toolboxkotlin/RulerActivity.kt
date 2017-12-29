package com.kolevmobile.toolboxkotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class RulerActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callerIntent = intent
        val activity_name = callerIntent.getIntExtra(ACTIVITY_NAME, 0)
        when (activity_name) {
            ACTIVITY_RULER -> setContentView(R.layout.activity_ruler)
            ACTIVITY_PROTRACTOR -> setContentView(R.layout.activity_protractor)
        }
    }

    companion object {

        internal val ACTIVITY_NAME = "Activity"
        internal val ACTIVITY_RULER = 1
        internal val ACTIVITY_PROTRACTOR = 2
    }

}
