package com.example.innocvkotlintask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.innocvkotlintask.data.UserModel
import com.example.innocvkotlintask.data.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepository: UserRepository? = null
    var userListLiveData: LiveData<List<UserModel>>? = null
    var userIdLiveData: LiveData<UserModel>? = null
    var addUserLiveData: LiveData<Boolean>
    var modifyUserLiveData: LiveData<Boolean>
    var deleteUserLiveData: LiveData<Boolean>? = null

    init {
        userRepository = UserRepository()
        userListLiveData = MutableLiveData()
        userIdLiveData = MutableLiveData()
        addUserLiveData = MutableLiveData(false)
        modifyUserLiveData = MutableLiveData(false)
        deleteUserLiveData = MutableLiveData()
    }

    fun getUsers() {
        userListLiveData = userRepository?.getUsers()
    }

    fun getUserById(id: Int) {
        userIdLiveData = userRepository?.getUserById(id)
    }

    fun addUser(userModel: UserModel) {
        addUserLiveData = userRepository?.addUser(userModel)!!
    }

    fun modifyUser(userModel: UserModel) {
        modifyUserLiveData = userRepository?.modifyUser(userModel)!!
    }

    fun deleteUser(id: Int) {
        deleteUserLiveData = userRepository?.deleteUser(id)
    }

}