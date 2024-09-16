/*
Vincent Hsiao - CIS 135 Final Project
AppState.kt

Contains information on the current trip


 */

package com.hsiao.tripbuddy.Data

import java.util.Date

data class AppState (
    var curId: Int = 0,
    var startDate: Date = Date(),
    var endDate: Date = Date(),
    var name: String = "",
    var cost: Int = 0

)