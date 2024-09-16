/*
Vincent Hsiao - CIS 135 Final Project
EventDAO.kt

DAO object for accessing EventDB: Contains methods to insert event and trips,
select all trips, select all events or according to tripId, update
trip cost, or delete trip or events


 */

package com.hsiao.tripbuddy.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.concurrent.Flow

/*
@Dao
interface EventDAO {
    @Insert
    fun insertEvent(event: Event)

    @Query("SELECT * FROM event")
    fun gatherAllEvents(): List<Event>

    @Query("SELECT * FROM event WHERE tripid = :tripId ORDER BY date ASC")
    fun gatherAllEventsBYId(tripId: Int): List<Event>
    }
*/

@Dao
interface EventDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrip(trip: TripC)

    @Query("SELECT * FROM trips")
    fun gatherAllTrips(): List<TripC>

    @Query("SELECT * FROM event")
    fun gatherAllEvents(): List<Event>

    @Query("SELECT * FROM event WHERE tripid = :tripId ORDER BY date ASC")
    fun gatherAllEventsBYId(tripId: Int): List<Event>

    @Query("UPDATE trips SET cost = :newCost WHERE tripId = :tripId")
    fun updateCost(tripId: Int, newCost: Int)

    @Query("DELETE FROM trips WHERE tripId = :tripId")
    fun deleteTrip(tripId: Int)

    @Query("DELETE FROM event WHERE tripid = :tripId")
    fun deleteTripEvents(tripId: Int)

    @Query("DELETE FROM event WHERE eventid = :eventId")
    fun deleteEvent(eventId: Int)


}