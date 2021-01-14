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
import com.revolve44.rickandmortycharacters.models.Character
import com.revolve44.rickandmortycharacters.models.RickAndMortyResponseItem
import com.revolve44.rickandmortycharacters.ui.MainScreenFragment
import com.revolve44.rickandmortycharacters.viewmodels.MainScreenViewModel
import java.util.*


class MainScreenRecycleviewAdapter(
    private val listener: MainScreenFragment,
        private val viewModel: MainScreenViewModel,
): RecyclerView.Adapter<MainScreenRecycleviewAdapter.MassiveViewHolder>(){

    private val list: MutableList<RickAndMortyResponseItem> = LinkedList()

    fun setData(newList: List<RickAndMortyResponseItem>){
        list.clear()
        list.addAll(newList)

        notifyDataSetChanged()
    }

    //private lateinit var mainViewModel : MainViewModel

//    fun CartListAdapter(list: List<PairNameandPassword>,
//                        listener: MainScreenFragment) {
//        context = context
//        cartModels = cartModels
//        cartViewModel = ViewModelProviders.of(context as FragmentActivity).get(CartViewModel::class.java)
//    }
//    init {
//    var context: Context
//    mainViewModel = ViewModelProviders.of(context as MainActivity).get(MainViewModel::class.java)
//    }
    private var lastPosition = -1
    lateinit var contextx: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MassiveViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.character_item,
                parent, false)
        contextx = parent.context

        return MassiveViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MassiveViewHolder, position: Int) {
        val currentItem = list[position]

//        holder.nameOfCharacter.text = list.get(position).name
//        holder.idOfCharacter.text = list.get(position).id.toString()

        holder.nameOfCharacter.text = list.get(position).name
        holder.idOfCharacter.text = list.get(position).id.toString()
        Glide.with(holder.itemView.getContext())
                .load(list.get(position).image)
                .into(holder.avatarOfCharacter);






        //holder.textView2.marqueeRepeatLimit
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(contextx, android.R.anim.slide_in_left)
            //val anim : RecyclerView.ItemAnimator =
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }

    }


    override fun getItemCount()= list.size

    inner class MassiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),


        View.OnClickListener {
        //val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val avatarOfCharacter: ImageView = itemView.findViewById(R.id.avatar)
        val nameOfCharacter: TextView = itemView.findViewById(R.id.name_of_character)
        val idOfCharacter: TextView = itemView.findViewById(R.id.id_of_character)







        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
            //showPopupMenu(v)
        }
//        private fun insert(): (View) -> Unit = {
//            layoutPosition.also { currentPosition ->
//                //list.add(currentPosition, uniqueString(string) to false)
//                notifyItemInserted(currentPosition)
//            }
//        }
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
//    fun removeItemRange(positionStart: Int, itemCount: Int) {
//        repeat(itemCount) {
//            this.items.removeAt(positionStart)
//        }
//        notifyItemRangeRemoved(positionStart, itemCount)
//    }


}