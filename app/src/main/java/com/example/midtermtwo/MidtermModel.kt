package com.example.midtermtwo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MidtermModel: ViewModel(){
    private lateinit var restInterface: MidtermAPIService
    val key = mutableStateOf("")
    val items = mutableStateOf<List<Item>>(emptyList())

    init {
        // Initialize Retrofit and MidtermAPIService inside the ViewModel
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://cmsc106.net/cafe/")
            .build()

        restInterface = retrofit.create(MidtermAPIService::class.java)

        // Fetch items
        viewModelScope.launch {
            fetchItems()
        }
    }

    private fun fetchItems() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fetchedItems = restInterface.getItems()
                items.value = fetchedItems
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



     fun fetchOrderId(
        cart: List<Item>,
        name: String,
        phoneNumber: String,
        onOrderPlaced: (Int) -> Unit
    ) {
        // Convert SnapshotStateList<Item> to a List of item IDs (Int)
        val itemIds = cart.map { it.id }  // Assuming 'id' is the field in your Item class

        // Create the Order object with name, phone, and the list of item IDs
        val order = Order(name = name, phone = phoneNumber, items = itemIds)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Use the order object for the API call
                val orderId = restInterface.placeOrder(order)  // This will pass the Order object
                withContext(Dispatchers.Main) {
                    onOrderPlaced(orderId)  // Pass the order ID back to the callback
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
