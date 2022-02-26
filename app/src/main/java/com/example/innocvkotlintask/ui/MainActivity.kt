package com.example.innocvkotlintask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.innocvkotlintask.R
import com.example.innocvkotlintask.data.UserModel
import com.example.innocvkotlintask.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UserAdapter.UserListener {

    private lateinit var vm: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = ViewModelProvider(this)[UserViewModel::class.java]
        initAdapter()
        vm.getUsers()
        vm.userListLiveData?.observe(this, Observer {
            progressbar.visibility = View.VISIBLE
            if (it!=null){
                rvTask.visibility = View.VISIBLE
                adapter.setData(it as ArrayList<UserModel>)
            }else{
                showToast(R.string.str_api_reading_error)
            }
            progressbar.visibility = View.GONE
        })

    }

    private fun initAdapter() {
        adapter = UserAdapter(this)
        rvTask.layoutManager = LinearLayoutManager(this)
        rvTask.adapter = adapter
    }

    override fun onItemDeleted(userModel: UserModel, position: Int) {
        userModel.id?.let { vm.deleteUser(it) }
        vm.deleteUserLiveData?.observe(this, Observer {
            if (it!=null){
                adapter.removeData(position)
            }else{
                showToast(R.string.str_delete_user_error)
            }
        })
    }

    private fun showToast(msg: Int){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

}
