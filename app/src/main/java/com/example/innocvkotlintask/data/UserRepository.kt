package com.example.innocvkotlintask.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.innocvkotlintask.network.ApiClient
import com.example.innocvkotlintask.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    private var apiInterface: ApiInterface? = null

    init {
        apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
    }

    fun getUsers(): LiveData<List<UserModel>> {

        val data = MutableLiveData<List<UserModel>>()

        apiInterface?.getUsers()?.enqueue(object : Callback<List<UserModel>> {

            override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                    call: Call<List<UserModel>>,
                    response: Response<List<UserModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else {
                    data.value = null
                }
            }
        })
        return data
    }

    fun getUserById(id: Int): LiveData<UserModel> {

        val data = MutableLiveData<UserModel>()

        apiInterface?.getUserById(id)?.enqueue(object : Callback<UserModel> {
            override fun onResponse(call:
                                    Call<UserModel>,
                                    response: Response<UserModel>) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else {
                    data.value = null
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                data.value = null
            }
        })

        return data
    }

    fun addUser(userModel: UserModel): LiveData<Boolean> {

        val callSuccess = MutableLiveData<Boolean>()

        apiInterface?.addUser(userModel)?.enqueue(object : Callback<UserModel> {

            override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>) {
                callSuccess.value = response.code() == 200
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
               callSuccess.value = false
            }
        })

        return callSuccess

    }

    fun modifyUser(userModel: UserModel): LiveData<Boolean> {

        val callSuccess = MutableLiveData<Boolean>()

        apiInterface?.modifyUser(userModel)?.enqueue(object : Callback<UserModel> {

            override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>) {
                callSuccess.value = response.code() == 200
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                callSuccess.value = false
            }
        })

        return callSuccess

    }

    fun deleteUser(id: Int): LiveData<Boolean> {

        val data = MutableLiveData<Boolean>()

        apiInterface?.deleteUser(id)?.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                data.value = false
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                data.value = response.code() == 200
            }

        })

        return data

    }

}