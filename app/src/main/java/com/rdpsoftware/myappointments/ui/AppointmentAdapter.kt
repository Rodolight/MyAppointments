package com.rdpsoftware.myappointments.ui

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rdpsoftware.myappointments.Models.Appointment
import com.rdpsoftware.myappointments.R


class AppointmentAdapter : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {
    var appointments = ArrayList<Appointment>()

    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
       fun bind(appointment:Appointment) = with(itemView){
               findViewById<TextView>(R.id.tvAppointment).text =
                        context.getString(R.string.item_appointnment_id,appointment.id)
               findViewById<TextView>(R.id.tvDoctor).text = appointment.doctor.name
               findViewById<TextView>(R.id.tvScheduledDate).text =
                        context.getString(R.string.item_appointnment_date,appointment.scheduledDate)
               findViewById<TextView>(R.id.tvScheduledTime).text =
                        context.getString(R.string.item_appointnment_time,appointment.scheduleTime)
            findViewById<TextView>(R.id.tvStatus).text = appointment.status
            findViewById<TextView>(R.id.tvSpecialty).text = appointment.specialty.name
            findViewById<TextView>(R.id.tvTypeAppointment).text = appointment.type
            val createdAt = appointment.createdAt.substring(0,10)+" "+ appointment.createdAt.substring(11,19)
            findViewById<TextView>(R.id.tvAppointmentDate).text = context.getString(R.string.text_appointment_date, createdAt)
            findViewById<TextView>(R.id.tvDescription).text = appointment.description

           var btnExpand = findViewById<ImageButton>(R.id.ibExpand)
           var llDetail = findViewById<LinearLayout>(R.id.llExpandable)

             btnExpand.setOnClickListener{
                 TransitionManager.beginDelayedTransition(parent as ViewGroup, AutoTransition())
                 if(llDetail.visibility == View.VISIBLE){
                     llDetail.visibility = View.GONE
                     btnExpand.setImageResource(R.drawable.ic_arrow_down_24)
                 }else{
                     llDetail.visibility = View.VISIBLE
                     btnExpand.setImageResource(R.drawable.ic_arrow_up_24)
                 }
             }
            }
       }

    // Inflate XML items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_appointment,parent,false))
    }
    // Binds Data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val appointment =  appointments[position]

        holder.bind(appointment)

    }

    // Number of elements
    override fun getItemCount() = appointments.size

}
