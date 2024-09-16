/*
Vincent Hsiao - CIS 135 Final Project
AddTrip.kt - Contains the add trip screen,
connects to the viewmodel to collect form data, and use
DAO and Repository to add data into database (with ExecutorService)


 */

package com.hsiao.tripbuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun addTripScreen(navController: NavController, viewModel: TripViewModel){
    var c = LocalContext.current
    Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly, modifier =  Modifier.fillMaxWidth().fillMaxHeight(0.5f).padding(top = 15.dp, start = 5.dp, end = 5.dp)){


        Text(text = "Add Trip", modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .padding(30.dp),
            fontSize = 35.sp,
            fontWeight = FontWeight.W600,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center)
        Row {
            Text("Name: ")
            TextField(value = viewModel.nameFieldValue, onValueChange = {viewModel.updateNameValue(it)})
        }


        Column {

            Text("Start Date (MM/DD/YYYY): ")
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

        Column {

            Text("End Date (MM/DD/YYYY): ")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                TextField(
                    value = viewModel.endDateValue,
                    onValueChange = { viewModel.updateEndDate(it) },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )


                Button(onClick = { viewModel.fillTodayDateEndDateVal() }) {
                    Text("Today's\n Date")
                }
            }
        }
        Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(10.dp)){
            Button(onClick = { viewModel.addTrip(c)
                navController.navigate("home")}) {
                Text(text = "Submit")
            }

            Button(onClick = { navController.navigate("home")
                viewModel.clearForm()}) {
                Text(text = "Return Home")

            }
        }

    }


}