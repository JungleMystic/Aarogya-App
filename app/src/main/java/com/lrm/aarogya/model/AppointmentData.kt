package com.lrm.aarogya.model

data class AppointmentData (
    var doctorName: String = "",
    var doctorSpecialization: String = "",
    var hospitalName: String = "",
    var hospitalAddress: String = "",
    var patientName: String = "",
    var patientAge: String = "",
    var patientGender: String = "",
    var appointmentDate: String = "",
    var appointmentTime: String = "",
    var patientProblem: String = "",
    var appointmentId: String = ""
)