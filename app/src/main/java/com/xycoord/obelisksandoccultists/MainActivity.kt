package com.xycoord.obelisksandoccultists

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myButton = findViewById(R.id.myHoveringButton) as FloatingActionButton
        val myText = findViewById(R.id.myText) as TextView
        val drawer = findViewById(R.id.nestedScrollView) as NestedScrollView
        val drawerButtonTemp = findViewById(R.id.myHoveringButton2) as FloatingActionButton

        myButton.setOnClickListener {
            if(myText.text.toString() == getString(R.string.test))
                myText.text = getString(R.string.test2)
            else
                myText.text = getString(R.string.test)
        }

        drawerButtonTemp.setOnClickListener {
            if(drawer.visibility == View.VISIBLE)
                drawer.visibility = View.GONE
            else
                drawer.visibility = View.VISIBLE
        }

    }
}
