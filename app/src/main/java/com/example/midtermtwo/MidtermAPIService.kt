package com.example.midtermtwo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MidtermAPIService{

@GET("items")
suspend fun getItems(): List<Item>
    @POST("orders")
    suspend fun placeOrder(@Body order: Order): Int

}
