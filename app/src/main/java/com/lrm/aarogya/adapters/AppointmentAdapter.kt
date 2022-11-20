package com.lrm.aarogya.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lrm.aarogya.R
import com.lrm.aarogya.model.AppointmentData

class AppointmentAdapter(
    val context: Context, val appointmentList: ArrayList<AppointmentData>
): RecyclerView.Adapter<AppointmentAdapter.UserViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentAdapter.UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_appointment_details, parent, false)

        return UserViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val appointments = appointmentList[position]

        holder.apt_id.text = appointments.appointmentId

        holder.patient_name.text = appointments.patientName
        holder.patient_age.text = appointments.patientAge
        holder.patient_gender.text = appointments.patientGender
        holder.apt_date.text = appointments.appointmentDate
        holder.apt_time.text = appointments.appointmentTime
        holder.patientProblem.text = appointments.patientProblem

        holder.doctorName.text = appointments.doctorName
        holder.doctorSpecialization.text = appointments.doctorSpecialization

        holder.hospitalName.text = appointments.hospitalName
        holder.hospitalAddress.text = appointments.hospitalAddress
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    class UserViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view) {

        val apt_id = view.findViewById<TextView>(R.id.apt_id)

        val patient_name = view.findViewById<TextView>(R.id.apt_patient_name)
        val patient_age = view.findViewById<TextView>(R.id.apt_patient_age)
        val patient_gender = view.findViewById<TextView>(R.id.apt_patient_gender)
        val apt_date = view.findViewById<TextView>(R.id.apt_appointment_date)
        val apt_time = view.findViewById<TextView>(R.id.apt_appointment_time)
        val patientProblem = view.findViewById<TextView>(R.id.apt_patient_problem)

        val doctorName = view.findViewById<TextView>(R.id.apt_doctor_name)
        val doctorSpecialization = view.findViewById<TextView>(R.id.apt_doctor_specialization)

        val hospitalName = view.findViewById<TextView>(R.id.apt_hospital_name)
        val hospitalAddress = view.findViewById<TextView>(R.id.apt_hospital_address)



        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}