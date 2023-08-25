import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object Network {

    private val retrofit = Retrofit.Builder()                  // создаем каркас для нашего retrofit - запускаем builder
        .baseUrl("http://www.boredapi.com/api/")               // ссылка на api
        .addConverterFactory(GsonConverterFactory.create())    // gson конвертер, для конвертации в наш класс
        .build()

    // получаем экземпляр интерфейса через retrofit (не знаю как это правильно назвать - сборка retrofit или как то так)
    val apiService: BoredApiService = retrofit.create(BoredApiService::class.java)

    // собственно сам интерфейс для запроса в сеть
    interface BoredApiService {
        // метод получения ActivityResponse из сети
        @GET("activity/")
        suspend fun getRandomActivity(): ActivityResponse
    }

}

