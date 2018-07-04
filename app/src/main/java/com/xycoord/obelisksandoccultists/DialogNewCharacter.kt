package com.xycoord.obelisksandoccultists

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_new_character.*
import kotlinx.android.synthetic.main.dialog_new_character.view.*

class DialogNewCharacter : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val inflater = activity!!.layoutInflater.inflate(R.layout.dialog_new_character,null)
        inflater.button_dialog_newCharacter_create.setOnClickListener{v ->
            val name = inflater.editText_dialog_newCharacter_name.text.toString()
            if (name != "") createNewCharacter(name)
            else Toast.makeText(activity, getString(R.string.no_name_error), Toast.LENGTH_SHORT).show()
        }
        inflater.button_dialog_newCharacter_cancel.setOnClickListener { v -> dismiss() }
        val builder = AlertDialog.Builder(activity).setView(inflater)
        val dialog = builder.create()
        return dialog

    }

    fun createNewCharacter(name : String){
        dismiss()
        Toast.makeText(activity, name, Toast.LENGTH_SHORT).show()
        launchCharacterActivity()
    }

    fun launchCharacterActivity(){
        val intent = Intent(activity, CharacterActivity::class.java)
        startActivity(intent)
    }

}