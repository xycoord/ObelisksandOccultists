package com.xycoord.obelisksandoccultists

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val TAG = "ObelisksAndOccultists"
    val COLLECTION_USERS = "users"

    lateinit var fbAuth : FirebaseAuth
    lateinit var fbDB : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fbAuth = FirebaseAuth.getInstance()
        fbDB = FirebaseFirestore.getInstance()

        dataTests()

        setSupportActionBar(activ_main_tool)
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
        val currentUser = fbAuth.currentUser
        updateUI(currentUser)
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
        user.put("first", "Ada")
        user.put("last", "Lovelace")
        user.put("born", 1815)

        fbDB.collection(COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()) }
                .addOnFailureListener{e ->  Log.w(TAG, "Error adding document", e);}

        fbDB.collection(COLLECTION_USERS)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        for (document in task.result) {
                            Log.d(TAG, document.id + " => " + document.data)
                        }
                }
    }


    private fun SignOut(){
        fbAuth.signOut()
        launchLoginActivity()
    }



}
