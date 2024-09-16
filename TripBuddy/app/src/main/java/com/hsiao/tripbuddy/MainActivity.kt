/*
Vincent Hsiao - CIS 135 Final Project
July 29, 2024
MainActivity.kt - Contains the main NavHost navigation controller,
the main home screen, and the "trip overview" screen

The Data is accessed with the viewModel, and the current trip (which is displayed in the trip overview) is stored
in Viewmodel's UIState object

The other two pages (add event and add trip) are in AddEvent.kt and AddTrip.kt, respectively

 */


package com.hsiao.tripbuddy

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hsiao.tripbuddy.Data.Event
import com.hsiao.tripbuddy.Data.EventDB
import com.hsiao.tripbuddy.Data.TripC
import com.hsiao.tripbuddy.ui.theme.TripBuddyTheme
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripBuddyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())

fun toastMsg(context: Context, msg: String, length: Int){
    Toast.makeText(context, msg, length)
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {


    val context: Context = LocalContext.current

    val viewModel: TripViewModel = viewModel()

    val database by lazy { EventDB.getDatabase(context).EventDAO() }

    viewModel.setVMContext(context)
    viewModel.initDB()

    val navController = rememberNavController()



    NavHost(navController = navController, startDestination = "home") {
        composable("home"){
            launchHomeScreen(navController = navController, viewModel, modifier)
        }
        composable("add_trip"){
            addTripScreen(navController = navController, viewModel = viewModel)
        }
        composable("trip_page"){
            launchTripOverviewScreen(navController = navController, viewModel)
        }
        composable("add_to_trip"){
            addEvent(navController = navController, viewModel = viewModel)
        }
    }


}


@Composable
fun launchHomeScreen(navController: NavController, viewModel: TripViewModel, modifier: Modifier){
    // test data
   // var tripsList: List<Array<Any>> = listOf(arrayOf(0, "Trip to Seoul and Tokyo", "6/3/2024", "7/20/2024", 3500),arrayOf(1, "Trip to the DMZ", "7/15/2024", "7/20/2024", 500))
    var tripsList: List<TripC> = viewModel.getAllTrips()
    val sdf = SimpleDateFormat("mm/dd/yyyy")


    Column (modifier = Modifier.padding(top=30.dp, start = 10.dp, end = 10.dp)){
        Text(
            text = "TripBuddy",
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(30.dp),
            fontSize = 35.sp,
            fontWeight = FontWeight.W600,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center
        )

        if(tripsList.isEmpty()){
            Text("No Trips yet. \nAdd some with the + Button.")
        }
        else {
            LazyColumn(modifier = Modifier.fillMaxHeight(0.85f)) {
                items(tripsList) { rowItems ->
                    Row {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp, 10.dp)
                                .border(2.dp, color = Color.Black, RoundedCornerShape(10.dp)),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.padding(10.dp).fillMaxWidth(0.75f)) {
                                // name
                                Text(
                                    text = "${rowItems.name}",
                                    fontSize = 25.sp
                                )
                                // date1 - date2
                                Text(
                                    text = "${sdf.format(rowItems.startDate)} - ${
                                        sdf.format(
                                            rowItems.endDate
                                        )
                                    }", fontSize = 18.sp
                                )
                            }

                            Button(onClick = {
                                viewModel.updateID(rowItems.tripId.toString().toInt())
                                viewModel.updateBasedOnTripC(rowItems)
                                navController.navigate("trip_page")
                            }, modifier = Modifier.padding(10.dp)) {
                                Text("->")
                            }
                        }


                    }
                }
            }
        }
        Button(onClick = { navController.navigate("add_trip") }, modifier = Modifier
            .size(60.dp)
            .align(Alignment.End)) {
            Text(text = "+", fontSize = 30.sp, textAlign = TextAlign.Center)
        }
    }
    
}

@Composable
fun eventDisp(e: Event, viewModel: TripViewModel, navController: NavController, modifier: Modifier){
    Row (modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp)), horizontalArrangement = Arrangement.Center){
        Column {
            Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(10.dp)){

                Text(text = "${e.name}", fontSize = 25.sp, modifier = Modifier.fillMaxWidth(0.6f))
                Text("Type: ${e.type}")
            }

            Text(text = "${e.date}")


        }
        Column {

            Text("$${e.cost}", modifier = Modifier.padding(10.dp))
            Text(text = " X ", modifier = Modifier
                .clickable {
                    e.cost?.let { viewModel.deleteEvent(e.eventid, it) }
                    navController.navigate("home")
                    navController.navigate("trip_page")
                }
                .padding(15.dp, 10.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(5.dp)))
        }

    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun launchTripOverviewScreen(navController: NavController, viewModel: TripViewModel){

    val sdf = SimpleDateFormat("mm/dd/yyyy")
    var eventList: List<Event>  = viewModel.getEventsBasedOnTripID(viewModel.getTripId())
    val context = LocalContext.current


    Column (modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(10.dp, 30.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween){
        Row (horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()){

            Button(onClick = { navController.navigate("home") }) {
                Text(text = "<-")
            }
            Text(text = viewModel.uiState.value.name, fontSize = 40.sp, modifier = Modifier.padding(20.dp, 0.dp))
        }
        Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
            Text(text = "${sdf.format(viewModel.uiState.value.startDate)} - ${sdf.format(viewModel.uiState.value.endDate)}")

            Text(text = "$${viewModel.uiState.value.cost} USD")

        }
        Divider()
        if(eventList.isEmpty()){
            Text("No Events for ${viewModel.uiState.value.name} yet. \nAdd some with the + Button.")
        }

        LazyColumn (modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(10.dp, 50.dp)){
            items(eventList) {
                event ->
                Row(modifier = Modifier.padding(bottom = 5.dp)) {
                    eventDisp(e = event, viewModel = viewModel, navController, modifier = Modifier)
                }
            }
        }




        Column (verticalArrangement = Arrangement.Bottom, horizontalAlignment= Alignment.End, modifier = Modifier.fillMaxSize()){
            Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                Button(onClick = { navController.navigate("add_to_trip") }) {
                    Text(text = "+ event")
                }

                Button(onClick = {
                    viewModel.deleteTrip(viewModel.getTripId())
                    navController.navigate("home")
                    toastMsg(context, "Trip Deleted!", Toast.LENGTH_SHORT)
                }){
                    Text("Delete Trip!")
                }
            }



        }

    }



}
