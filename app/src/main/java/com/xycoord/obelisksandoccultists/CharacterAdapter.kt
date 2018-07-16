package com.xycoord.obelisksandoccultists

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.custom_row_character.view.*

class CharacterAdapter (private var items: ArrayList<String>) : RecyclerView.Adapter<CustomViewHolder>(){

    val fbDB = FirebaseFirestore.getInstance()
    lateinit var parent: ViewGroup

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.custom_row_character, parent,false)
        this.parent = parent
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val characterId = items.get(position)
        fbDB.collection(MainActivity.COLLECTION_CHARACTERS).document(characterId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(MainActivity.TAG, "success")
                        holder.itemView.tV_cRow_name.text = task.result.getString(MainActivity.CHARACTER_NAME)

                    }
                    else Log.d(MainActivity.TAG, "unsucessful")
                }
        holder.itemView.setOnClickListener { v ->

                Log.d(MainActivity.TAG, holder.itemView.tV_cRow_name.text as String)
                val intent = Intent(parent.context, CharacterActivity::class.java)
                intent.putExtra(MainActivity.TAG_CHARACTERID, characterId)
                parent.context.startActivity(intent)

        }
    }


}
class  CustomViewHolder(v:View): RecyclerView.ViewHolder(v) {}