package com.rdpsoftware.myappointments.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.rdpsoftware.myappointments.Models.Doctor
import com.rdpsoftware.myappointments.Models.Specialty
import com.rdpsoftware.myappointments.R
import com.rdpsoftware.myappointments.databinding.ActivityCreateAppointmentBinding
import com.rdpsoftware.myappointments.databinding.CardViewStepOneBinding
import com.rdpsoftware.myappointments.databinding.CardViewStepThreeBinding
import com.rdpsoftware.myappointments.databinding.CardViewStepTwoBinding
import com.rdpsoftware.myappointments.io.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CreateAppointmentActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCreateAppointmentBinding
    private val selectedCalendar: Calendar = Calendar.getInstance()
    private var selectedTimeRadioButton: RadioButton? = null

    private lateinit var step1 : CardViewStepOneBinding
    private lateinit var step2 : CardViewStepTwoBinding
    private lateinit var step3 : CardViewStepThreeBinding

    private val apiService by lazy {
        ApiService.create()
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        step1 = binding.includeStepOne
        step2 = binding.includeStepTwo
        step3 = binding.includeStepThree

        loadSpecialties()
        listenSpecialtyChanges()
        showStep2()
        showStep3()
        onClickScheduledDate()
        confirmAppointment()
    }

    private fun loadSpecialties(){
          val call = apiService.getSpecialties()
        call.enqueue(object:Callback<ArrayList<Specialty>>{
            override fun onResponse(call: Call<ArrayList<Specialty>>,response: Response<ArrayList<Specialty>> ) {
               if(response.isSuccessful){ //[200...300]
                   val specialties = response.body()
                   step1.spinnerSpecialties.adapter = ArrayAdapter(this@CreateAppointmentActivity,android.R.layout.simple_list_item_1, specialties!!.toMutableList())
               }
            }

            override fun onFailure(call: Call<ArrayList<Specialty>>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity,getString(R.string.error_loading_specialties), Toast.LENGTH_SHORT).show()
                finish()
            }

        })
    }

    private fun listenSpecialtyChanges(){
            step1.spinnerSpecialties.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               val specialty = adapter?.getItemAtPosition(position) as Specialty
                loadDoctors(specialty.id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemClick(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun loadDoctors(specialtyId: Int){
       val call = apiService.getDoctors(specialtyId)
       call.enqueue(object : Callback<ArrayList<Doctor>> {
           override fun onResponse(call: Call<ArrayList<Doctor>>, response: Response<ArrayList<Doctor>>) {
               if(response.isSuccessful){ //[200...300]
                   val doctors = response.body()
                   step2.spinnerDoctors.adapter = ArrayAdapter(this@CreateAppointmentActivity,android.R.layout.simple_list_item_1, doctors!!.toMutableList())
               }
           }

           override fun onFailure(call: Call<ArrayList<Doctor>>, t: Throwable) {
               Toast.makeText(this@CreateAppointmentActivity,getString(R.string.error_loading_doctors), Toast.LENGTH_SHORT).show()
               finish()
           }

       })
    }

    private fun showStep2(){
        step1.btnNext.setOnClickListener {
                if (binding.includeStepOne.etDescription.text.toString().length < 3) {
                    binding.includeStepOne.etDescription.error = getString(R.string.validate_create_appointment_description)
                } else {
                    binding.includeStepOne.cvStep1.visibility = View.GONE
                    binding.includeStepTwo.cvStep2.visibility = View.VISIBLE
                }
        }
    }

    private fun showStep3(){
        binding.includeStepTwo.btnResume.setOnClickListener {
            when {
                binding.includeStepTwo.etScheduledDate.text.isEmpty() -> {
                    binding.includeStepTwo.etScheduledDate.error = getString(R.string.validate_create_appointment_date)
                }
                selectedTimeRadioButton == null -> {
                    Snackbar.make(binding.createAppointmentLinearLayout, R.string.validate_create_appointment_time, Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    showAppointmentDataToConfirm()
                    binding.includeStepTwo.cvStep2.visibility = View.GONE
                    binding.includeStepThree.cvStep3.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun confirmAppointment(){
        binding.includeStepThree.btnConfirm.setOnClickListener{
            Toast.makeText(this, "Cita registrada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun onClickScheduledDate(){
        binding.includeStepTwo.etScheduledDate.setOnClickListener(){
            val year = selectedCalendar.get(Calendar.YEAR)
            val month = selectedCalendar.get(Calendar.MONTH)
            val dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_MONTH)
            val listener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                selectedCalendar.set(y,m,d)
            val selectedDate = "$y-${(m+1).twoDigits()}-${d.twoDigits()}"
                binding.includeStepTwo.etScheduledDate.setText(selectedDate)
                displayRadioButton()
                step2.etScheduledDate.error = null
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
        binding.includeStepTwo.rgLeft.removeAllViews()
        binding.includeStepTwo.rgRight.removeAllViews()
        selectedTimeRadioButton = null

        val hours = arrayOf("8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM")
        val hoursPM = arrayOf("3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM")

        hours.forEach {
                val radioButton = RadioButton(this)
                radioButton.id = View.generateViewId()
                radioButton.text = it
                radioButton.setOnClickListener{ view ->
                    selectedTimeRadioButton?.isChecked = false
                    selectedTimeRadioButton = view as RadioButton?
                    selectedTimeRadioButton?.isChecked = true
                }
                binding.includeStepTwo.rgLeft.addView(radioButton)
           }

        hoursPM.forEach {
            val radioButton2 = RadioButton(this)
            radioButton2.id = View.generateViewId()
            radioButton2.setOnClickListener{ view ->
                selectedTimeRadioButton?.isChecked = false
                selectedTimeRadioButton = view as RadioButton?
                selectedTimeRadioButton?.isChecked = true
            }
            radioButton2.text = it
            binding.includeStepTwo.rgRight.addView(radioButton2)

        }

    }

    private fun Int.twoDigits() = if(this>9) this.toString() else "0$this"

    override fun onBackPressed() {
        when {
            binding.includeStepThree.cvStep3.visibility == View.VISIBLE -> {
                binding.includeStepThree.cvStep3.visibility = View.GONE
                binding.includeStepTwo.cvStep2.visibility = View.VISIBLE
            }
            binding.includeStepTwo.cvStep2.visibility == View.VISIBLE -> {
                binding.includeStepTwo.cvStep2.visibility = View.GONE
                binding.includeStepOne.cvStep1.visibility = View.VISIBLE
            }
            else -> {
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

    private fun showAppointmentDataToConfirm(){
        binding.includeStepThree.confirmDescription.text = binding.includeStepOne.etDescription.text.toString()
        binding.includeStepThree.confirmSpecialty.text = binding.includeStepOne.spinnerSpecialties.selectedItem.toString()
        val selectedRadioId = binding.includeStepOne.radioGroupType.checkedRadioButtonId
        val selectedRadioType = binding.includeStepOne.radioGroupType.findViewById<RadioButton>(selectedRadioId)
        binding.includeStepThree.confirmType.text = selectedRadioType.text.toString()

        binding.includeStepThree.confirmDoctor.text = binding.includeStepTwo.spinnerDoctors.selectedItem.toString()
        binding.includeStepThree.confirmDate.text = binding.includeStepTwo.etScheduledDate.text.toString()
        binding.includeStepThree.confirmHour.text = selectedTimeRadioButton?.text.toString()
    }
}