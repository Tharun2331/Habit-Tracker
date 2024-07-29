package com.example.habittracker

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class fragment_home : Fragment(R.layout.fragment_home), HabitAdapter.OnItemClickListener {

    private lateinit var spinnerHabits: Spinner
    private lateinit var buttonAddHabit: Button
    private lateinit var recyclerViewHabits: RecyclerView
    private lateinit var habitAdapter: HabitAdapter
    private var selectedHabit: String? = null
    private val habitList = mutableListOf<Habit>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerHabits = view.findViewById(R.id.spinner_habits)
        buttonAddHabit = view.findViewById(R.id.button_add_habit)
        recyclerViewHabits = view.findViewById(R.id.recycler_view_habits)

        // Create an ArrayAdapter using the custom layouts
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.habits,
            R.layout.spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        spinnerHabits.adapter = adapter

        // Set the spinner listener
        spinnerHabits.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedHabit = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        // Set the button listener
        buttonAddHabit.setOnClickListener {
            selectedHabit?.let {
                val newHabit = Habit(it)
                habitList.add(newHabit)
                habitAdapter.notifyItemInserted(habitList.size - 1)
            }
        }

        // Set up RecyclerView
        habitAdapter = HabitAdapter(habitList, this)
        recyclerViewHabits.adapter = habitAdapter
        recyclerViewHabits.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onItemClick(position: Int) {
        val habitToRemove = habitList[position]
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete Habit")
            setMessage("Are you sure you want to delete the habit '${habitToRemove.name}'?")
            setPositiveButton("Yes") { _, _ ->
                habitList.removeAt(position)
                habitAdapter.notifyItemRemoved(position)
            }
            setNegativeButton("No", null)
        }.show()
    }
}
