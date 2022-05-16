package com.square.contacts

import com.google.gson.Gson
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class EmployeesDomain(
    private val url: String
) {

    private var client = OkHttpClient()

    fun getAllContacts(
        onReceive: (ArrayList<Employee>) -> Unit,
        onError: (String) -> Unit
    ) {

        val request: Request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .build()

        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(" Cannot Load Employees: Connection Error")
            }

            override fun onResponse(call: Call, response: Response) {
                val list: ArrayList<Employee> = ArrayList()
                if (response.isSuccessful) {
                    try {
                        val jsonObject = JSONObject(response.body!!.string())
                        val array = jsonObject.getJSONArray("employees")
                        (0 until array.length()).forEach {
                            val employee = array.getString(it)
                            val employeeObject = Gson().fromJson(employee, Employee::class.java)
                            if (validateEmployee(employeeObject)) list.add(employeeObject)
                        }
                        onReceive(list)
                    } catch (exception: JSONException) {
                        onError("Cannot Load Employees: JSON format error ")
                    }
                } else {
                    onError("Cannot Load Employees: ${response.message}")
                }
            }
        })
    }

    /**
     * Checks for null values due to Gson deserialization
     */
    private fun validateEmployee(employee: Employee) =
        employee.id != null &&
                employee.name != null &&
                employee.email != null &&
                employee.team != null &&
                employee.employeeType != null


}




