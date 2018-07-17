package com.xycoord.obelisksandoccultists

import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_character.*
import kotlinx.android.synthetic.main.app_bar_character.*
import kotlinx.android.synthetic.main.content_character.*
import android.text.Editable
import android.text.TextWatcher



class CharacterActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var characterId: String
    lateinit var fbDB: FirebaseFirestore

    var characterName: String? = ""
    var characterDescriptor: String? = ""
    var characterType: String? = ""
    var characterFocus: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        setSupportActionBar(toolbar)

        characterId = intent.getStringExtra(MainActivity.TAG_CHARACTERID).toString()
        fbDB = FirebaseFirestore.getInstance()
        fbDB.collection(MainActivity.COLLECTION_CHARACTERS).document(characterId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        characterName = task.result.getString(MainActivity.CHARACTER_NAME)
                        characterDescriptor = task.result.getString(MainActivity.CHARACTER_DESCRIPTOR)
                        characterType = task.result.getString(MainActivity.CHARACTER_TYPE)
                        characterFocus = task.result.getString(MainActivity.CHARACTER_FOCUS)
                        Log.d(MainActivity.TAG, task.result.id + " => " + characterName)

                        title = characterName
                        eT_character_name.setText(characterName)
                        eT_character_descripter.setText(characterDescriptor)
                        eT_character_type.setText(characterType)
                        eT_character_focus.setText(characterFocus)

                        eT_character_name.addTextChangedListener(textWatcher)
                        eT_character_descripter.addTextChangedListener(textWatcher)
                        eT_character_type.addTextChangedListener(textWatcher)
                        eT_character_focus.addTextChangedListener(textWatcher)
                    }
                    else Log.d(MainActivity.TAG, "unsucessful")

                }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)



    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            val field = HashMap<String, Any>()
            Log.d(MainActivity.TAG, s.hashCode().toString() + " " + eT_character_descripter.hashCode().toString())
            when (s.hashCode()) {
                eT_character_name.text.hashCode() -> field.put(MainActivity.CHARACTER_NAME, s.toString())
                eT_character_descripter.text.hashCode() -> field.put(MainActivity.CHARACTER_DESCRIPTOR, s.toString())
                eT_character_type.text.hashCode() -> field.put(MainActivity.CHARACTER_TYPE, s.toString())
                eT_character_focus.text.hashCode() -> field.put(MainActivity.CHARACTER_FOCUS, s.toString())
                else -> {return}
            }
            fbDB.collection(MainActivity.COLLECTION_CHARACTERS).document(characterId).update(field)
        }
        override fun afterTextChanged(s: Editable) {}
    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.character, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {}
            R.id.nav_gallery -> {}
            R.id.nav_slideshow -> {}
            R.id.nav_manage -> {}
            R.id.nav_share -> {}
            R.id.nav_send -> {}
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
