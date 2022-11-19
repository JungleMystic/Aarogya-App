package com.lrm.aarogya.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lrm.aarogya.R
import com.lrm.aarogya.model.Hospital
import de.hdodenhof.circleimageview.CircleImageView

class HospitalAdapter(
    val context: Context, val hospitalList: ArrayList<Hospital>
): RecyclerView.Adapter<HospitalAdapter.UserViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalAdapter.UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_hospital_data, parent, false)

        return UserViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val hospitals = hospitalList[position]

        holder.hospital_nameText.text = hospitals.hospitalName
        holder.hospital_Address.text = hospitals.hospital_address

        if (hospitals.hospital_pic == "") {
            Glide.with(context).load(R.drawable.hospital_logo3).into(holder.hospital_Pic)
        } else {
            Glide.with(context).load(hospitals.hospital_pic).placeholder(R.drawable.hospital_logo3).into(holder.hospital_Pic)
        }
    }

    override fun getItemCount(): Int {
        return hospitalList.size
    }

    class UserViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view) {
        val hospital_nameText = view.findViewById<TextView>(R.id.hospital_name_text)
        val hospital_Pic = view.findViewById<CircleImageView>(R.id.hospital_pic)
        val hospital_Address = view.findViewById<TextView>(R.id.hospital_address_text)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}