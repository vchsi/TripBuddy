/*
Vincent Hsiao - CIS 135 Final Project
TripC.kt

The entity container for trip, with primary key of
tripId (relates to event). Date objects are handled with the
TypeConverter.kt class


 */

package com.hsiao.tripbuddy.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "trips")
data class TripC(
    @PrimaryKey(autoGenerate = true)
    val tripId: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "startDate") val startDate: Date,
    @ColumnInfo(name = "date") val endDate: Date,
    @ColumnInfo(name = "cost") val cost: Int?,

    )