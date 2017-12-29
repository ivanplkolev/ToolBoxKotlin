package com.kolevmobile.toolboxkotlin


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView

import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)
        Picasso.with(this).load(R.drawable.background_image).fit().into(findViewById<View>(R.id.main_layout_bkg) as ImageView)
        Picasso.with(this).load(R.drawable.toolbox_icon).into(findViewById<View>(R.id.main_layout_icon) as ImageView)

    }

    fun startRuler(v: View) {
        val intent = Intent(this@MainActivity, RulerActivity::class.java)
        intent.putExtra(RulerActivity.ACTIVITY_NAME, RulerActivity.ACTIVITY_RULER)
        this@MainActivity.startActivity(intent)
    }

    fun startCompass(v: View) {
        //        Intent compassIntent = new Intent(MainActivity.this, CompassActivity.class);
        //        MainActivity.this.startActivity(compassIntent);
    }

    fun startLevel(v: View) {
        val levelIntent = Intent(this@MainActivity, LevelActivity::class.java)
        this@MainActivity.startActivity(levelIntent)
    }

    fun startProtractor(v: View) {
        val intent = Intent(this@MainActivity, RulerActivity::class.java)
        intent.putExtra(RulerActivity.ACTIVITY_NAME, RulerActivity.ACTIVITY_PROTRACTOR)
        this@MainActivity.startActivity(intent)
    }

    fun startAboutActivity(v: View?) {
        val aboutIntent = Intent(this@MainActivity, AboutActivity::class.java)
        this@MainActivity.startActivity(aboutIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.startAboutActivity -> {
                startAboutActivity(null)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
