# Cafe Order App

## Overview
The Cafe Order App is a simple Android application built using Jetpack Compose. It allows customers to view a list of items available for order, select items, add them to a cart, and place an order. The app interacts with a backend server to fetch items and post orders.

## Features
- Display a list of items available to order.
- Select items by tapping on them.
- Add selected items to an order.
- View the items in the cart.
- Enter user details and place an order.
- Receive an order number upon successful order placement.

## Screenshots
(Include screenshots of the app's views if available)

## Server Interactions
The backend server for this app is hosted at [https://cmsc106.net/cafe](https://cmsc106.net/cafe).

### Endpoints
1. **Fetch Items**
   - **URL:** `https://cmsc106.net/cafe/items`
   - **Method:** GET
   - **Response Structure:**
     ```json
     [
       {
         "id": <integer id of item>,
         "name": <name of item>,
         "cost": <how much the item costs (String)>
       },
       ...
     ]
     ```

2. **Post Order**
   - **URL:** `https://cmsc106.net/cafe/orders`
   - **Method:** POST
   - **Request Structure:**
     ```json
     {
       "name": <name of person placing the order>,
       "phone": <phone number>,
       "items": <array of ints>
     }
     ```
   - **Response:** The server will respond with a status code of 200 and the integer order number for the newly created order.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/cafe-order-app.git
## Usage

1. Open the project in Android Studio.
2. Build and run the project on an Android device or emulator.
3. Launch the app.
4. View the list of items available for order.
5. Tap on an item to select it.
6. Press the "Add to Order" button to add the selected item to the cart.
7. After selecting all items, navigate to the order view.
8. Enter your name and phone number.
9. Click the "Place Order" button to send the order.
10. The server will respond with an order number, and the app will empty the cart.

### Technologies Used
- Android
- Jetpack Compose
- Retrofit
- Kotlin

### Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new Pull Request.



