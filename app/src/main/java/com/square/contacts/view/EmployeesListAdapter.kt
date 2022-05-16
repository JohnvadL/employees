package com.square.contacts.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.square.contacts.Employee
import com.square.contacts.R
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class EmployeesListAdapter : RecyclerView.Adapter<EmployeesListAdapter.EmployeeViewHolder>() {

    private var dataset: ArrayList<Employee> = ArrayList()

    private var sortCategory: Int = 0

    class EmployeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val summary: TextView = view.findViewById(R.id.summary)
        val team: TextView = view.findViewById(R.id.team)
        val type: TextView = view.findViewById(R.id.type)
        val profilePicture: ImageView = view.findViewById(R.id.profile_picture)
        val loadImage: Button = view.findViewById(R.id.load_image_button)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.employee_card, viewGroup, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: EmployeeViewHolder, position: Int) {
        viewHolder.name.text = dataset[position].name
        viewHolder.team.text = dataset[position].team
        viewHolder.type.text = dataset[position].employeeType

        viewHolder.loadImage.isEnabled = true
        viewHolder.loadImage.text = "Load Image"
        viewHolder.loadImage.visibility = View.GONE

        viewHolder.summary.text = with(dataset[position].biography) {
            if (this.isNullOrBlank()) "This employee does not have a summary yet!" else this
        }

        val imageLoadCallback = object : Callback {
            override fun onSuccess() {
                viewHolder.loadImage.visibility = View.GONE
            }

            override fun onError(e: java.lang.Exception?) {
                if (viewHolder.loadImage.visibility == View.GONE) {
                    viewHolder.loadImage.visibility = View.VISIBLE
                } else {
                    Log.e("EmployeesListAdapter", " Error Loading Image")
                    viewHolder.loadImage.text = "Error: Retry?"
                    viewHolder.loadImage.isEnabled = true
                }
            }
        }

        Picasso.get()
            .load(dataset[position].photoSmall)
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(viewHolder.profilePicture, imageLoadCallback)

        viewHolder.loadImage.setOnClickListener {
            it.isEnabled = false
            (it as Button).text = "Loading .."
            Picasso.get()
                .load(dataset[position].photoSmall)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(viewHolder.profilePicture, imageLoadCallback)
        }
    }

    override fun getItemCount() = dataset.size

    /**
     * Update employee list, using Diffutils here
     * for performance when sorting
     */
    private fun setEmployeeList(newData: ArrayList<Employee>) {
        val callback = EmployeeCallback(dataset, newData)
        val result = DiffUtil.calculateDiff(callback)
        dataset.clear()
        dataset.addAll(newData)
        result.dispatchUpdatesTo(this)
    }

    fun sortByCategory(category: Int = sortCategory, data: ArrayList<Employee> = dataset) {
        val sortedList = ArrayList<Employee>(data)
        sortCategory = category
        when (category) {
            INDEX_NAME -> sortedList.sortBy { it.name }
            INDEX_TEAM -> sortedList.sortBy { it.team }
            INDEX_TYPE -> sortedList.sortBy { it.employeeType }
        }
        setEmployeeList(sortedList)
    }

    fun updateEmployees(employees: ArrayList<Employee>) {
        sortByCategory(
            data = employees
        )
    }

    internal class EmployeeCallback(
        private val oldData: List<Employee>,
        private val newData: List<Employee>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldData.size

        override fun getNewListSize() = newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition].id == newData[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldData[oldItemPosition]
            val newItem = newData[newItemPosition]
            return oldItem.name == newItem.name &&
                    oldItem.team == newItem.team &&
                    oldItem.biography == newItem.biography &&
                    oldItem.photoSmall == newItem.photoSmall &&
                    oldItem.employeeType == newItem.employeeType
        }
    }

    companion object {
        const val INDEX_NAME = 0
        const val INDEX_TEAM = 1
        const val INDEX_TYPE = 2
    }
}