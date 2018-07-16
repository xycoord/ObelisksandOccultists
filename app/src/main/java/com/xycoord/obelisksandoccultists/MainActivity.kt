package com.xycoord.obelisksandoccultists

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    lateinit var fbAuth : FirebaseAuth
    lateinit var fbDB : FirebaseFirestore
    lateinit var currentUser : FirebaseUser



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fbAuth = FirebaseAuth.getInstance()
        fbDB = FirebaseFirestore.getInstance()

        setSupportActionBar(activ_main_tool)
        FABNewCharacter.setOnClickListener{view -> launchNewCharacter()}
    }

    private fun launchNewCharacter() {
        val dialog = DialogNewCharacter()
        dialog.show(supportFragmentManager, "dlg")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater  = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == R.id.signOutButton){
                Toast.makeText(this,
                        "You have been signed out!",
                        Toast.LENGTH_SHORT)
                        .show()
                SignOut()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        currentUser = fbAuth.currentUser!!
        updateUI(currentUser)
        dataTests()
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if (currentUser!=null) {
            textView_Name.text = getString(R.string.signed_in) + " " + currentUser.displayName
        }
        else launchLoginActivity()
    }

    private fun launchLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun dataTests(){
        val user = HashMap<String, Any>()
        user.put("name", currentUser.displayName.toString())
        user.put("email", currentUser.email.toString())

        fbDB.collection(COLLECTION_USERS).document(currentUser.uid)
                .set(user, SetOptions.merge())
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added ") }
                .addOnFailureListener{e ->  Log.w(TAG, "Error adding document", e);}

        getCharacters(currentUser.uid)
    }

    private fun getCharacters(uid: String) {
        val charactersArray = ArrayList<String>()
        fbDB.collection(COLLECTION_USERS).document(uid)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "sucessful")
                        for (doc in task.result.data!!) {
                            if (doc.component1() == "characters" && doc.component2().javaClass == HashMap::class.java) {
                                for (pair in doc.component2() as HashMap<String, Boolean>) {
                                    charactersArray.add(pair.component1())
                                }
                            }
                        }
                        setupRecyclerCharacters(charactersArray)
                    }
                    else Log.d(TAG, "unsucessful")
                }

    }
    private fun setupRecyclerCharacters(characterIds: ArrayList<String>){
        Log.d(TAG, characterIds.toString())
        character_recycler.layoutManager = LinearLayoutManager(this)
        character_recycler.adapter = CharacterAdapter(characterIds)
    }

    private fun SignOut(){
        fbAuth.signOut()
        launchLoginActivity()
    }

    companion object {
        val TAG = "ObelisksAndOccultists"
        val COLLECTION_USERS = "users"
        val COLLECTION_CHARACTERS = "characters"
        val TAG_CHARACTERID = "characterId"
        val SUBCOLLECTION_USERS_CHARACTERS = "users_characters"
        val CHARACTER_NAME = "name"
    }
}
