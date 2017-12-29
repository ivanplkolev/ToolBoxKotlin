package com.kolevmobile.toolboxkotlin


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView

import com.squareup.picasso.Picasso


class AboutActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar!!.title = getString(R.string.string_about_activity)
        toolbar!!.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_24dp)
        Picasso.with(this).load(R.drawable.background_image).fit().into(findViewById<View>(R.id.main_layout_bkg) as ImageView)
        toolbar!!.setNavigationOnClickListener { onBackPressed() }
    }


    fun sendMail(v: View) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getString(R.string.string_about_email), null))
        startActivity(Intent.createChooser(emailIntent, "Contact Ivan Kolev"))
    }

    fun openWeb(v: View) {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.string_about_linkedin)))
        startActivity(i)
    }

}
