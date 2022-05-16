package com.square.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmployeesViewModel : ViewModel() {

    var contacts: MutableLiveData<ArrayList<Employee>> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()

    private val contactsDomain = EmployeesDomain(contactsUrl)

    fun getAllContacts() {
        contactsDomain.getAllContacts({
            contacts.postValue(it)
        }, {
            errorMessage.postValue(it)
        })
    }

    companion object {
        private const val contactsUrl =
            "https://s3.amazonaws.com/sq-mobile-interview/employees.json"
        private const val contactsUrlMalformed =
            "https://s3.amazonaws.com/sq-mobile-interview/employees_malformed.json"
        private const val contactsUrlEmpty =
            "https://s3.amazonaws.com/sq-mobile-interview/employees_empty.json"

    }
}