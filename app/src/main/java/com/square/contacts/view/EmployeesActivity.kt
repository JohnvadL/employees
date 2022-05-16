package com.square.contacts.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.square.contacts.EmployeesViewModel
import com.square.contacts.R

class EmployeesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val employeesViewModel: EmployeesViewModel = EmployeesViewModel()
    private val employeesListAdapter = EmployeesListAdapter()
    private lateinit var employeeList: RecyclerView

    private val tag = this::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val noEmployees: TextView = findViewById(R.id.no_employees_message)
        val swipeView: SwipeRefreshLayout = findViewById(R.id.swipe_view)
        val spinner: Spinner = findViewById(R.id.sort_selector)

        employeeList = findViewById(R.id.employee_list)
        employeeList.adapter = employeesListAdapter
        employeeList.layoutManager = LinearLayoutManager(this)

        // Logic for swipe to refresh
        swipeView.isRefreshing = true
        swipeView.setOnRefreshListener {
            employeesViewModel.getAllContacts()
        }

        // Listen to request response
        employeesViewModel.contacts.observe(
            this
        ) {
            swipeView.isRefreshing = false
            if (it.isNotEmpty()) {
                noEmployees.visibility = View.INVISIBLE
                employeeList.visibility = View.VISIBLE
                employeesListAdapter.updateEmployees(it)
            } else {
                Log.d(tag, " No Employees in list, Showing message")
                employeeList.visibility = View.INVISIBLE
                noEmployees.visibility = View.VISIBLE
            }
        }

        // Listen to Errors
        employeesViewModel.errorMessage.observe(this) {
            swipeView.isRefreshing = false
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        // Let user configure sort type
        ArrayAdapter.createFromResource(
            this,
            R.array.sorting_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
        employeesViewModel.getAllContacts()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        // Account for recyclerview being mid update when we sort
        employeeList.post {
            employeesListAdapter.sortByCategory(category = position)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}


