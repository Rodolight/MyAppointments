package com.rdpsoftware.myappointments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import com.rdpsoftware.myappointments.databinding.ActivityCreateAppointmentBinding
import java.util.*

class CreateAppointmentActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCreateAppointmentBinding
    private val selectedCalendar: Calendar = Calendar.getInstance()
    private var selectedRadioButton: RadioButton? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAppointmentBinding.inflate(this.layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnNext.setOnClickListener {
            if (binding.etDescription.text.toString().length < 3) {
                binding.etDescription.error = getString(R.string.validate_create_appointment_description)
            } else {
                binding.cvStep1.visibility = View.GONE
                binding.cvStep2.visibility = View.VISIBLE
            }
        }

        binding.btConfirm.setOnClickListener{
            Toast.makeText(this, "Cita registrada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }

        val specialtiesOptions = arrayOf("Specialty A","Specialty B","Specialty C")
        binding.spinnerSpecialties.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,specialtiesOptions)

        val doctorsOptions = arrayOf("Doctor A","Doctor B","Doctor C")
        binding.spinnerDoctors.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,doctorsOptions)

        onClickScheduledDate()
    }

    private fun onClickScheduledDate(){
        binding.etScheduledDate.setOnClickListener(){
            val year = selectedCalendar.get(Calendar.YEAR)
            val month = selectedCalendar.get(Calendar.MONTH)
            val dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_MONTH)
            val listener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                selectedCalendar.set(y,m,d)
                binding.etScheduledDate.setText("$y-${(m+1).twoDigits()}-${d.twoDigits()}")
                displayRadioButton()
            }

             val datePickerDialog = DatePickerDialog(this,listener,year,month,dayOfWeek)
             val datePicker = datePickerDialog.datePicker
             val calendar = Calendar.getInstance()
              calendar.add(Calendar.DAY_OF_MONTH, 1)
              datePicker.minDate = calendar.timeInMillis
              calendar.add(Calendar.DAY_OF_MONTH,29)
              datePicker.maxDate = calendar.timeInMillis


              datePickerDialog.show()
        }
    }

    private fun displayRadioButton() {
        binding.rgLeft.removeAllViews()
        binding.rgRight.removeAllViews()
        selectedRadioButton = null

        val hours = arrayOf("8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM")
        val hoursPM = arrayOf("3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM")

        hours.forEach {
                val radioButton = RadioButton(this)
                radioButton.id = View.generateViewId()
                radioButton.text = it
                radioButton.setOnClickListener{ view ->
                    selectedRadioButton?.isChecked = false
                    selectedRadioButton = view as RadioButton?
                    selectedRadioButton?.isChecked = true
                }
                binding.rgLeft.addView(radioButton)
           }

        hoursPM.forEach {
            val radioButton2 = RadioButton(this)
            radioButton2.id = View.generateViewId()
            radioButton2.setOnClickListener{ view ->
                selectedRadioButton?.isChecked = false
                selectedRadioButton = view as RadioButton?
                selectedRadioButton?.isChecked = true
            }
            radioButton2.text = it
            binding.rgRight.addView(radioButton2)

        }

    }

    private fun Int.twoDigits() = if(this>9) this.toString() else "0$this"

    override fun onBackPressed() {
        if (binding.cvStep2.visibility == View.VISIBLE) {
            binding.cvStep2.visibility = View.GONE
            binding.cvStep1.visibility = View.VISIBLE
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.dialog_create_appointment_exit_title))
                    .setMessage(getString(R.string.dialog_create_appointment_exit_message))
                    .setPositiveButton(getString(R.string.dialog_create_appointment_exit_positive_btn)) { _, _ ->
                        finish()
                    }
                    .setNegativeButton(getString(R.string.dialog_create_appointment_exit_negative_btn)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(false)
            val dialog = builder.create()
            dialog.show()
        }
    }
}