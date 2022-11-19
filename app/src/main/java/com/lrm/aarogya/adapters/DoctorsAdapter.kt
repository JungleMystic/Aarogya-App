package com.lrm.aarogya.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.aarogya.R
import com.lrm.aarogya.model.Doctor
import de.hdodenhof.circleimageview.CircleImageView

class DoctorsAdapter(
    val context: Context, val doctorsList: ArrayList<Doctor>
): RecyclerView.Adapter<DoctorsAdapter.UserViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsAdapter.UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_doctor_data, parent, false)

        return UserViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val doctors = doctorsList[position]

        holder.doctor_nameText.text = doctors.doctorName
        holder.doctor_specialization_text.text = doctors.doctorSpecialization
        holder.doctor_experience_text.text = doctors.doctorExperience
        holder.doctor_availability_text.text = doctors.doctorAvailability

        if (doctors.doctorPic == "") {
            Glide.with(context).load(R.drawable.doctor_icon2).into(holder.doctor_Pic)
        } else {
            Glide.with(context).load(doctors.doctorPic).placeholder(R.drawable.doctor_icon2).into(holder.doctor_Pic)
        }
    }

    override fun getItemCount(): Int {
        return doctorsList.size
    }

    class UserViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view) {
        val doctor_Pic = view.findViewById<CircleImageView>(R.id.doctor_pic)
        val doctor_nameText = view.findViewById<TextView>(R.id.doctor_name_text)
        val doctor_specialization_text = view.findViewById<TextView>(R.id.doctor_specialization_text)
        val doctor_experience_text = view.findViewById<TextView>(R.id.doctor_experience_text)
        val doctor_availability_text = view.findViewById<TextView>(R.id.doctor_availability_text)


        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}