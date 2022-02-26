package com.example.simulacro

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Results (val results: List<Usuario>): Parcelable

@Parcelize
data class Usuario (val gender: String, val name: Name, val location: Location): Parcelable

@Parcelize
data class Name (val title: String, val first: String, val last: String): Parcelable

@Parcelize
data class Location (val city: String) : Parcelable