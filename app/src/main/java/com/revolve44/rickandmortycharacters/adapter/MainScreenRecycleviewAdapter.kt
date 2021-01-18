package com.revolve44.rickandmortycharacters.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.revolve44.rickandmortycharacters.R
import com.revolve44.rickandmortycharacters.base.BaseAdapter
import com.revolve44.rickandmortycharacters.base.BaseViewHolder
import com.revolve44.rickandmortycharacters.base.ItemElementsDelegate
import com.revolve44.rickandmortycharacters.models.Character
import com.revolve44.rickandmortycharacters.models.RickAndMortyResponseItem
import com.revolve44.rickandmortycharacters.ui.MainScreenFragment
import com.revolve44.rickandmortycharacters.viewmodels.MainScreenViewModel
import java.util.*


class MainScreenRecycleviewAdapter(): BaseAdapter<Character>(){

    var pairDelegate: ItemElementsDelegate<Character>? = null

    // for clicks
    fun attachDelegate(callback: ItemElementsDelegate<Character>) {
        this.pairDelegate = callback
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Character> {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.character_item,parent,false))

    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<Character>(itemView = itemView){

        val avatarOfCharacter: ImageView = itemView.findViewById(R.id.avatar)
        val nameOfCharacter: TextView = itemView.findViewById(R.id.name_of_character)
        val idOfCharacter: TextView = itemView.findViewById(R.id.id_of_character)
        val deleteItemAction: ImageView = itemView.findViewById(R.id.delete_item)

        override fun bind(model: Character) {
            idOfCharacter.text = model.id.toString()
            nameOfCharacter.text = model.name
            Glide.with(avatarOfCharacter)
                .load(model.img_path)
                .into(avatarOfCharacter);
            deleteItemAction.setOnClickListener {
                pairDelegate?.onElementClick(model,itemView,this.adapterPosition)



            }

        }
    }
}