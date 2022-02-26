package com.example.innocvkotlintask.viewmodel

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.innocvkotlintask.data.UserModel
import com.example.innocvkotlintask.data.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepository: UserRepository? = null
    var userListLiveData: LiveData<List<UserModel>>? = null
    var addUserLiveData:LiveData<UserModel>?=null
    var deleteUserLiveData:LiveData<Boolean>?=null

    init {
        userRepository = UserRepository()
        userListLiveData = MutableLiveData()
        addUserLiveData = MutableLiveData()
        deleteUserLiveData = MutableLiveData()
    }

    fun getUsers() {
        userListLiveData = userRepository?.getUsers()
    }

    fun addUser(userModel: UserModel){
        addUserLiveData = userRepository?.addUser(userModel)
    }

    fun deleteUser(id:Int){
        deleteUserLiveData = userRepository?.deletePost(id)
    }

}