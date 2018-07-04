package com.xycoord.obelisksandoccultists

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_new_character.view.*

class DialogNewCharacter : DialogFragment() {

    lateinit var fbDB: FirebaseFirestore

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        fbDB = FirebaseFirestore.getInstance()

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_new_character,null)

        view.button_dialog_newCharacter_create.setOnClickListener{ v ->
            val name = view.editText_dialog_newCharacter_name.text.toString()
            if (name != "") createNewCharacter(name)
            else Toast.makeText(activity, getString(R.string.no_name_error), Toast.LENGTH_SHORT).show()
        }
        view.button_dialog_newCharacter_cancel.setOnClickListener {
            v -> dismiss()
        }

        val builder = AlertDialog.Builder(activity).setView(view)
        return builder.create()
    }

    fun createNewCharacter(name : String){



        val newCharacterMap = HashMap<String, Any>()
        val context = activity
        newCharacterMap.put("name", name)
        fbDB.collection(MainActivity.COLLECTION_CHARACTERS)
                .add(newCharacterMap)
                .addOnSuccessListener { documentReference ->
                    if (context != null) launchCharacterActivity(context, documentReference.id)
                }
                .addOnFailureListener{e ->  Log.w(MainActivity.TAG, "Error adding document", e);}
        dismiss()
        Toast.makeText(activity, name, Toast.LENGTH_SHORT).show()
    }

    private fun launchCharacterActivity(context: FragmentActivity, characterId: String){
        Log.d(MainActivity.TAG, characterId)
        val intent = Intent(context, CharacterActivity::class.java)
        intent.putExtra(MainActivity.TAG_CHARACTERID, characterId)
        context.startActivity(intent)
    }

}