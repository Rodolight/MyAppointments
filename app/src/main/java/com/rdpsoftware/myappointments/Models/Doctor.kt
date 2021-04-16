package com.rdpsoftware.myappointments.Models

data class Doctor (val id: Int, val name: String ) {
    override fun toString(): String {
        return name
    }
}