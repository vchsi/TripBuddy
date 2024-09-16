/*
Vincent Hsiao - CIS 135 Final Project
Event.kt

Event entity for the EventDB, to store a trip's events (determined by TripID) in
the database


 */

package com.hsiao.tripbuddy.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val eventid: Int = 0,
    @ColumnInfo(name = "tripid") val tripId: Int?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "cost") val cost: Int?,

)