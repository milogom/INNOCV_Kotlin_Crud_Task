package com.example.innocvkotlintask.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.innocvkotlintask.R
import com.example.innocvkotlintask.data.UserModel
import kotlinx.android.synthetic.main.user_item.view.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class UserAdapter(var listener: UserListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var data : ArrayList<UserModel>?=null

    interface UserListener{
        fun onItemDeleted(userModel: UserModel, position: Int)
        fun onCreate(savedInstanceState: Bundle?)
    }

    fun setData(list: ArrayList<UserModel>){
        data = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bindView(item)
        holder.itemView.setOnClickListener {
            item?.let { it1 ->
                listener.onItemDeleted(it1, position)
            }
        }
    }

    fun addData(userModel: UserModel) {
        data?.add(0,userModel)
        notifyItemInserted(0)
    }

    fun removeData(position: Int) {
        data?.removeAt(position)
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(item: UserModel?) {
            itemView.tvUserId.text = item?.id.toString()
            itemView.tvUserName.text= item?.name
            itemView.tvUserBirthDate.text= getFormatedDate(item?.birthdate )
        }

        @SuppressLint("NewApi")
        private fun getFormatedDate(fullSqlDate: String?): String? {
            val formattedString = fullSqlDate?.substring(0, 19) + ".000Z"
            return DateTimeFormatter.ofPattern("dd/MMM/uuuu").format(OffsetDateTime.parse(formattedString))
        }
    }
}