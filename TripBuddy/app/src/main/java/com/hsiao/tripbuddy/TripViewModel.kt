/*
Vincent Hsiao - CIS 135 Final Project
July 29, 2024
TripViewModel.kt
Contains all of the logic in the program. Allows for connections between the front end
Composables to the EventDB and helper classes. The current data on the trip is stored in UIState and _UIState objects



 */

package com.hsiao.tripbuddy

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hsiao.tripbuddy.Data.AppState
import com.hsiao.tripbuddy.Data.Event
import com.hsiao.tripbuddy.Data.EventDAO
import com.hsiao.tripbuddy.Data.EventDB
import com.hsiao.tripbuddy.Data.TripC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.Executors

class TripViewModel: ViewModel() {

    lateinit var db: EventDB
    var nameFieldValue by mutableStateOf("")
        private set

    var startDateValue by mutableStateOf("")
        private set

    var endDateValue by mutableStateOf("")
        private set

    var newCost by mutableStateOf("")
        private set

    var type by mutableStateOf("")
        private set

    lateinit var ctx: Context
    lateinit var dao: EventDAO
    lateinit var eventsList: LiveData<List<Event>>

    private val executor = Executors.newSingleThreadExecutor()

    private var _uistate = MutableStateFlow(AppState())
    var uiState: StateFlow<AppState> = _uistate.asStateFlow()

  //  val userDAO = Database.EventDAO()

    fun updateID(newId: Int){
        _uistate.update {
                currentState ->
            currentState.copy(
               curId = newId)
        }
    }

    fun setVMContext(c: Context){
        ctx = c
    }

    fun initDB(){
        db = EventDB.getDatabase(ctx)
        dao = db.EventDAO()
    }

    fun updateNameValue(newName: String){
        nameFieldValue = newName
    }
    fun updateStartDate(newName: String){
        startDateValue = newName
    }
    fun updateEndDate(newName: String){
        endDateValue = newName
    }

    fun updateCost(newCostV: String){
        newCost = newCostV
    }

    fun updateType(newCostV: String){
        type = newCostV
    }

    fun getTripId(): Int{
        return uiState.value.curId
    }



    fun dateConvert(strDate: String, pattern: String, context: Context): Date {
        val sdf = SimpleDateFormat(pattern)
        try {
            return sdf.parse(strDate) as Date
        } catch (E: Exception){
            Toast.makeText(context, E.toString(), Toast.LENGTH_LONG).show()
            return Date()
        }
    }

    fun clearForm(){
        nameFieldValue = ""
        startDateValue = ""
        endDateValue = ""
        type = ""
        newCost = ""
    }

    fun addTrip(context: Context){
        val trip = TripC(name = nameFieldValue, startDate = dateConvert(startDateValue, "mm/dd/yyyy", context), endDate = dateConvert(endDateValue,"mm/dd/yyyy", context), cost = 0)
        executor.execute(
            kotlinx.coroutines.Runnable {
                dao.insertTrip(trip)
            }
        )


        Toast.makeText(ctx, "Success!", Toast.LENGTH_LONG).show()
        clearForm()

    }

    fun addEvent(tripId: Int){
        // the type variable is assigned to the name box in this case, so we make the switch accordingly
        try {
            val event: Event = Event(
                tripId = tripId,
                name = type,
                type = nameFieldValue,
                cost = newCost.toInt(),
                date = dateConvert(startDateValue, "MM/dd/yyyy", ctx)

            )

            executor.execute(
                kotlinx.coroutines.Runnable {
                    dao.insertEvent(event)
                }
            )

            event.cost?.let { updateCost(it) }


            println("Successfully added!")
            Toast.makeText(ctx, "Successfully added event!", Toast.LENGTH_LONG).show()
            clearForm()
        } catch (e: Exception){
            Toast.makeText(ctx, "Failed! ${e}", Toast.LENGTH_LONG).show()
        }

    }

    fun getEventsBasedOnTripID(tripId: Int): List<Event> {
        var retVal: List<Event> = listOf()

        // println("$tripId | $retVal")

        return dao.gatherAllEventsBYId(tripId)

    }

    fun getAllTrips(): List<TripC> {
        return dao.gatherAllTrips()
    }


    fun updateBasedOnTripC(tripC: TripC){
        _uistate.update {
                currentState ->
            currentState.copy(
                curId = tripC.tripId.toString().toInt(),
                startDate = tripC.startDate,
                endDate = tripC.endDate,
                name = tripC.name.toString(),
                cost = tripC.cost.toString().toInt()
            )
        }
    }


    fun fillTodayDateStartDateVal() {
        startDateValue = SimpleDateFormat("MM/dd/YYYY").format(Date())
    }

    fun fillTodayDateEndDateVal() {
        endDateValue = SimpleDateFormat("MM/dd/YYYY").format(Date())
    }

    fun updateCost(incCost: Int){
        executor.execute(
            kotlinx.coroutines.Runnable {
                dao.updateCost(uiState.value.curId, uiState.value.cost + incCost)
                _uistate.update {
                        currentState ->
                    currentState.copy(
                        cost = uiState.value.cost + incCost
                    )
                }
            }
        )



    }

    fun clearState(){
        _uistate = MutableStateFlow(AppState())
        uiState = _uistate.asStateFlow()
    }

    fun deleteEvent(eventId: Int, cost: Int){
        executor.execute(
            kotlinx.coroutines.Runnable {
                dao.deleteEvent(eventId)
                dao.updateCost(uiState.value.curId, uiState.value.cost - cost)

                _uistate.update {
                        currentState ->
                    currentState.copy(
                        cost = uiState.value.cost - cost
                    )
                }
            }
        )

    }

    fun deleteTrip(tripId: Int){
        executor.execute(
            kotlinx.coroutines.Runnable {
                dao.deleteTripEvents(tripId)
                dao.deleteTrip(tripId)
            }
        )

        Toast.makeText(ctx, "Successfully Deleted ${uiState.value.name} trip", Toast.LENGTH_LONG)
        clearState()
    }


}