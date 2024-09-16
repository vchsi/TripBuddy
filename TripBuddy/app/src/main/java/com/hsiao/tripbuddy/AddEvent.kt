/*
Vincent Hsiao - CIS 135 Final Project
AddEvent.kt - Contains the add event screen,
connects to the viewmodel to collect form data, and use
DAO and Repository to add data into database (with ExecutorService),
similar to AddTrip screen.


 */

package com.hsiao.tripbuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun addEvent(navController: NavController, viewModel: TripViewModel){
    val id = viewModel.uiState.collectAsState().value.curId
    Column (modifier = Modifier.padding(15.dp, 40.dp)){
        Text(text = "Add Event to trip \"${viewModel.uiState.collectAsState().value.name}\"", fontSize = 25.sp)

        Row (modifier = Modifier.padding(10.dp)){
            Text("Type: ")
            TextField(value = viewModel.nameFieldValue, onValueChange = {viewModel.updateNameValue(it)})
        }

        Row (modifier = Modifier.padding(10.dp)){
            Text("Name: ")
            TextField(value = viewModel.type, onValueChange = {viewModel.updateType(it)})
        }

        Row (modifier = Modifier.padding(10.dp)){
            Text("Cost: ")
            TextField(value = viewModel.newCost, onValueChange = {viewModel.updateCost(it)})

        }
        Column {

            Text("Date (MM/DD/YYYY): ")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                TextField(
                    value = viewModel.startDateValue,
                    onValueChange = { viewModel.updateStartDate(it) },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )


                Button(onClick = { viewModel.fillTodayDateStartDateVal() }) {
                    Text("Today's\n Date")
                }
            }
        }

        Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {

            Button(onClick = { navController.navigate("trip_page")
            viewModel.clearForm()}) {
                Text(text = "Back")

            }

            Button(onClick = {
                viewModel.addEvent(viewModel.uiState.value.curId)
                navController.navigate("trip_page")
            }) {
                Text(text = "Submit")
            }

        }
    }


}