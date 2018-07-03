package com.xycoord.obelisksandoccultists

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    val TAG = "ObelisksAndOccultists"
    val RC_SIGN_IN = 9001
    val COLLECTION_USERS = "users"

    lateinit var fbAuth : FirebaseAuth
    lateinit var fbDB : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val gac = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        fbAuth = FirebaseAuth.getInstance()
        fbDB = FirebaseFirestore.getInstance()

        signInButton.setOnClickListener{view -> SignIn(gac)}
        signOutButton.setOnClickListener{view -> SignOut()}
        dataTests()
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

    override fun onStart() {
        super.onStart()
        val currentUser = fbAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if (currentUser!=null) {
            textView_Name.text = currentUser.displayName
        }
        else textView_Name.text = getText(R.string.no_user_signed_in)
    }

    private fun SignIn(gac: GoogleApiClient){
        val singInIntent = Auth.GoogleSignInApi.getSignInIntent(gac)
        startActivityForResult(singInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if(result.isSuccess){
            val account = result.signInAccount
            if (account!=null) firebaseAuthWithGoogle(account)
        } else {
            textView_Name.text = result.isSuccess.toString()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val currentUser = fbAuth.currentUser
                        updateUI(currentUser)
                    } else {
                        Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                 }
    }

    private fun SignOut(){
        fbAuth.signOut()
        textView_Name.text = getString(R.string.no_user_signed_in)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
