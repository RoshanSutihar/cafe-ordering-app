package com.example.midtermtwo

import androidx.compose.material.icons.filled.ArrowBack
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.midtermtwo.ui.theme.MidtermTwoTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            MidtermTwoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MidtermApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}






@Composable
fun MidtermApp(modifier: Modifier = Modifier) {

    val cart = remember { mutableStateListOf<Item>() }  // Cart to store selected items
    val selectedItem = remember { mutableStateOf<Item?>(null) }  // Holds currently selected item

    val navController = rememberNavController()
    val midtermModel: MidtermModel = viewModel()
    // Define the onOrderPlaced function
    val onOrderPlaced: () -> Unit = {
        cart.clear()  // Empty cart once the order is placed
    }

    NavHost(navController, startDestination = "itemSelection") {
        composable("itemSelection") {
            ItemSelectionScreen(
                navController = navController,
                selectedItem = selectedItem,
                cart = cart,
                midtermModel = midtermModel
            )
        }
        composable("orderConfirmation") {
            OrderConfirmationScreen(
                midtermModel,
                navController = navController,
                cart = cart,
                onOrderPlaced = {
                    cart.clear()  // Empty cart once order is placed
                }
            )
        }
        composable("cartScreen") {
            CartScreen(
                navController = navController,
                cart = cart,
                onOrderPlaced = onOrderPlaced
            )
        }


    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSelectionScreen(
    midtermModel: MidtermModel,
    navController: NavController,
    selectedItem: MutableState<Item?>,
    cart: SnapshotStateList<Item>,  // This should be a mutable state list
    modifier: Modifier = Modifier
) {
    val itemList = midtermModel.items.value  // Access the list of items from the model

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Item Selection") }
                // Removed actions block to remove the cart icon
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(itemList) { item ->
                    ItemRow(item = item) {
                        selectedItem.value = item  // Select the item when tapped
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to add the selected item to the cart
            Button(
                onClick = {
                    selectedItem.value?.let {
                        cart.add(it)  // Add the selected item to the cart
                        Log.d("ItemSelection", "Item added to cart: ${it.name}")
                    }
                    selectedItem.value = null  // Clear selection
                },
                enabled = selectedItem.value != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Order")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigate to Order Confirmation Screen
            Button(
                onClick = { navController.navigate("orderConfirmation") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Cart & Place Order")
            }
        }
    }
}


@Composable
fun ItemRow(item: Item, onItemClick: () -> Unit) {
    // Item row UI
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(item.name, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.weight(1f))
            Text("${item.cost}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
@Composable
fun OrderConfirmationScreen(
    midtermModel: MidtermModel,
    navController: NavController,
    cart: SnapshotStateList<Item>,  // Ensure it's mutable
    onOrderPlaced: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var orderNumber by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Order Confirmation Title
        Text("Order Confirmation", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Display cart items
        if (cart.isEmpty()) {
            Text("Your cart is empty", style = MaterialTheme.typography.headlineMedium)
        } else {
            LazyColumn(modifier = Modifier
                .fillMaxWidth()  // Take up the full width
                .wrapContentHeight()  // Adjust height based on content size
                .padding(vertical = 8.dp)) {
                items(cart) { item ->
                    Text(item.name, modifier = Modifier.padding(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input fields for user details
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        // Place Order Button
        Button(
            onClick = {
                if (cart.isNotEmpty() && name.isNotBlank() && phoneNumber.isNotBlank()) {
                    // Fetch order ID asynchronously
                    midtermModel.fetchOrderId(cart, name, phoneNumber) { orderId ->
                        orderNumber = orderId.toString() // Set the order number
                        // Optional: Clear the cart after order is placed
                        cart.clear()  // Clear cart if desired, otherwise comment this out
                        onOrderPlaced()  // Notify that the order was placed
                    }
                }
            },
            enabled = cart.isNotEmpty() && name.isNotBlank() && phoneNumber.isNotBlank(),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Place Order")
        }

        // Display order number if order was placed
        orderNumber?.let {
            Text("Order Placed! Order Number: $it", modifier = Modifier.padding(top = 16.dp))
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cart: SnapshotStateList<Item>,
    onOrderPlaced: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                actions = {
                    IconButton(onClick = {
                        // You can handle any actions here, like going back to item selection
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Cart UI goes here
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (cart.isEmpty()) {
                Text("Your cart is empty.", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn {
                    items(cart) { item ->
                        Text(item.name)  // You can display more details about the item here
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Button to place the order
                Button(
                    onClick = {
                        onOrderPlaced()  // Trigger the order placement action
                        navController.navigate("orderConfirmation")  // Navigate to order confirmation screen
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Place Order")
                }
            }
        }
    }
}

