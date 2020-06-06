package com.project.pocketdoc.model.tables

data class DoctorType private constructor(
    val type: String
) {
    companion object {
        private val doctorRoles = hashMapOf<String, DoctorType>(
            "ENT" to DoctorType("Лор"),
            "therapist" to DoctorType("Терапевт"),
            "pediatrician" to DoctorType("Педиатр"),
            "neuropathologist" to DoctorType("Невропатолог"),
            "procedures" to DoctorType("Процедуры"),
            "analysis" to DoctorType("Анализы"),
            "massage" to DoctorType("Массаж")
        )

        fun getDoctorRole(key: String?) = doctorRoles[key]?.type
    }
}

