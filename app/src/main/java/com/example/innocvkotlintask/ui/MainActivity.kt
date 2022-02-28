package com.example.innocvkotlintask.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.innocvkotlintask.R
import com.example.innocvkotlintask.data.UserModel
import com.example.innocvkotlintask.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input_box.view.*
import kotlinx.android.synthetic.main.input_box.view.etBirthday
import kotlinx.android.synthetic.main.show_box.view.*
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity(), UserAdapter.UserListener, View.OnClickListener {

    private lateinit var vm: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = ViewModelProvider(this)[UserViewModel::class.java]
        initAdapter()
        btnAddUser?.setOnClickListener() { onItemCreated() }
        showList(vm, 0)
    }

    private fun initAdapter() {
        adapter = UserAdapter(this)
        rvTask.layoutManager = LinearLayoutManager(this)
        rvTask.adapter = adapter
    }

    override fun onItemCreated() {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.input_box, null)
        var name: String =""
        var birthday: String=""

        view.etBirthday?.setOnClickListener { showDatePickerDialog(view) }

        dialog.setContentView(view)

        view.btnSave?.setOnClickListener() {

            name = view.etName?.text.toString()
            if (view.etBirthday.text.isNotEmpty()) {
                birthday = getSqlDate(view.etBirthday?.text.toString()).toString()
            }

            if (!name.isNullOrEmpty() && !birthday.isNullOrEmpty()) {
                val userModel = UserModel()

                userModel.id = 0
                userModel.name = name
                userModel.birthdate = birthday

                vm.addUser(userModel)
                vm.addUserLiveData?.observe(this, Observer { successCall ->
                    if (successCall) {
                        var position: Int = adapter.itemCount
                        initAdapter()
                        showList(vm, position)
                    } else {
                        showToast(R.string.str_error_saving_user)
                    }
                    dialog.cancel()
                    showToast(R.string.str_user_created)
                })
            } else {
                showToast(R.string.str_error_empty_fields)
            }
        }

        dialog.show()
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onItemEdited(userModel: UserModel, position: Int) {
        showToast(getString(R.string.str_user_edit) + " " + userModel.id)
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.input_box, null)

        view.etName.setText(userModel.name)
        view.etBirthday.setText(getFormattedDate(userModel.birthdate))

        lateinit var name: String
        lateinit var birthday: String

        view.etBirthday?.setOnClickListener { showDatePickerDialog(view) }

        dialog.setContentView(view)

        view.btnSave?.setOnClickListener() {

            name = view.etName?.text.toString()
            if (view.etBirthday.text.isNotEmpty()) {
                birthday = getSqlDate(view.etBirthday?.text.toString()).toString()
            }

            if (name.isNotEmpty() && birthday.isNotEmpty()) {
                val userModelEdited = UserModel()
                userModelEdited.id = userModel.id
                userModelEdited.name = name
                userModelEdited.birthdate = birthday

                vm.modifyUser(userModelEdited)
                vm.modifyUserLiveData?.observe(this, Observer {
                    if (it) {
                        initAdapter()
                        showList(vm, position)
                    } else {
                        showToast(R.string.str_error_saving_user)
                    }
                    dialog.cancel()
                    showToast(R.string.str_user_saved)
                })
            } else {
                showToast(R.string.str_error_empty_fields)
            }
        }

        dialog.show()
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onItemDeleted(userModel: UserModel, position: Int) {
        userModel.id?.let { vm.deleteUser(it) }
        vm.deleteUserLiveData?.observe(this, Observer {
            if (it != null) {
                adapter.removeData(position)
                showToast(R.string.str_deleted_user)
            } else {
                showToast(R.string.str_delete_user_error)
            }
        })
    }

    override fun onItemClicked(userModel: UserModel, position: Int) {
        showToast(getString(R.string.str_user_show) + " " + userModel.id)
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.show_box, null)

        view.etIdLabel.setText(userModel.id.toString())
        view.etNameLabel.setText(userModel.name)
        view.etBirthdayLabel.setText(getFormattedDate(userModel.birthdate))

        dialog.setContentView(view)
        dialog.show()
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private fun showList(vm: UserViewModel, position: Int){
        vm.getUsers()
        vm.userListLiveData?.observe(this, Observer {
            progressbar.visibility = View.VISIBLE
            if (it != null) {
                rvTask.visibility = View.VISIBLE
                adapter.setData(it as ArrayList<UserModel>)
                rvTask.smoothScrollToPosition(position)
            } else {
                showToast(R.string.str_api_reading_error)
            }
            progressbar.visibility = View.GONE
        })
    }

    private fun showToast(msg: Int) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.etBirthday -> showDatePickerDialog(v)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getSqlDate(birthday: String): java.sql.Date {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val parsed: Date = format.parse(birthday)
        return java.sql.Date(parsed.time)
    }

    private fun showDatePickerDialog(v: View?) {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year, v) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int, v: View?) {
        val d: String = day.toString()
        val m: String = (month + 1).toString()
        val yyyy: String = year.toString()

        val dd: String = if (d.length == 1) {
            "0$d"
        } else {
            d
        }

        val mm: String = if (m.length == 1) {
            "0$m"
        } else {
            m
        }

        v?.etBirthday?.setText("$dd/$mm/$yyyy")
    }

    @SuppressLint("NewApi")
    fun getFormattedDate(fullSqlDate: String?): String? {
        val formattedString = if (fullSqlDate?.length!! > 10) {
            fullSqlDate.substring(0, 19) + ".000Z"
        } else {
            fullSqlDate + "T00:00:00.000Z"
        }
        return DateTimeFormatter.ofPattern("dd/MM/uuuu").format(OffsetDateTime.parse(formattedString))
    }

}
