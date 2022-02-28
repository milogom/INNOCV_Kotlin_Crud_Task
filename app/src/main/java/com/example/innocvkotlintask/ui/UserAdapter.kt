package com.example.innocvkotlintask.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.innocvkotlintask.R
import com.example.innocvkotlintask.data.UserModel
import kotlinx.android.synthetic.main.user_item.view.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class UserAdapter(var listener: UserListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var data: ArrayList<UserModel>? = null

    interface UserListener {
        fun onCreate(savedInstanceState: Bundle?)
        fun onItemCreated()
        fun onItemEdited(userModel: UserModel, position: Int)
        fun onItemDeleted(userModel: UserModel, position: Int)
        fun onItemClicked(userModel: UserModel, position: Int)
    }

    fun setData(list: ArrayList<UserModel>) {
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
        holder.itemView.user_delete_icon.setOnClickListener {
            item?.let { listener.onItemDeleted(it, position) }
        }
        holder.itemView.user_edit_icon.setOnClickListener {
            item?.let { listener.onItemEdited(it, position) }
        }
        holder.itemView.setOnClickListener {
            item?.let {listener.onItemClicked(it, position) }
        }
    }

    fun addData(userModel: UserModel) {
        data?.add(0, userModel)
        notifyItemInserted(0)
    }

    fun removeData(position: Int) {
        data?.removeAt(position)
        refreshList()
    }

    private fun refreshList() {
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: UserModel?) {
            itemView.tvUserId.text = item?.id.toString()
            itemView.tvUserName.text = item?.name
            itemView.tvUserBirthDate.text = getFormattedDate(item?.birthdate.toString())
        }

        @SuppressLint("NewApi")
        private fun getFormattedDate(fullSqlDate: String?): String? {
            val formattedString = if (fullSqlDate?.length!! > 10) {
                fullSqlDate.substring(0, 19) + ".000Z"
            } else {
                fullSqlDate + "T00:00:00.000Z"
            }
            return DateTimeFormatter.ofPattern("dd/MM/uuuu").format(OffsetDateTime.parse(formattedString))
        }
    }
}

