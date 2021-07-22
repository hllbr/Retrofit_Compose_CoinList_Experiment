package com.hllbr.retrofit_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hllbr.retrofit_compose.model.CryptoModel
import com.hllbr.retrofit_compose.service.CryptoAPI
import com.hllbr.retrofit_compose.ui.theme.Retrofit_ComposeTheme
import com.hllbr.retrofit_compose.ui.theme.hllbr9
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Retrofit_ComposeTheme {
                MainScreen()
                    //prices?key=a82399be10cc5c1dadff681c8df1eeefc123916b
                    //https://api.nomics.com/v1/prices?key=a82399be10cc5c1dadff681c8df1eeefc123916b

            }
        }
    }
}

@Composable
fun MainScreen(){

    val BASE_URL = "https://api.nomics.com/v1/"

    var cryptoModels = remember{ mutableStateListOf<CryptoModel>() }
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)

    val call = retrofit.getData()
    call.enqueue(object :Callback<List<CryptoModel>>{
        override fun onResponse(//cevap verildiğinde yapılacak lar
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>,
        ) {
            if (response.isSuccessful){
                response.body()?.let {
                    it.forEach {
                        println(it.currency)
                        println(it.price)
                        //Alınan liste
                        cryptoModels.addAll(listOf(it))
                    }
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            //işlem başarısız olduğunda ne yapacağız.
            t.printStackTrace()
        }

    })

    Scaffold(topBar = {AppBar()}) {
    CryptoList(cryptos = cryptoModels)
    }
}
@Composable
fun AppBar(){
    TopAppBar(contentPadding = PaddingValues(10.dp),
    backgroundColor = hllbr9) {
        Text(text = "Retrofit Compose",fontSize = 26.sp)
    }
}
@Composable
fun CryptoList(cryptos:List<CryptoModel>){
    /*
    LazyColumn gördüğümüzde hafıza ile ilgili işlemler olduğunu anlamamız gerekiyor.
    RecyclerView kullanmaktan dah mantıklı ve effective bir seçenek compose içerisinde tercih ediliebilir.
     */
    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(cryptos){crpto->
            CryptoRow(crypto = crpto)
        }
    }
}
@Composable
fun CryptoRow(crypto : CryptoModel){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.surface)) {
        Text(text = crypto.currency,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(2.dp),
        fontWeight = FontWeight.Bold)
        Text(text = crypto.price,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(2.dp))
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Retrofit_ComposeTheme {
        CryptoRow(crypto = CryptoModel("XRP","56900000"))
    }
}