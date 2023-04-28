package cz.idio.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static final String BASE_URL = "http://remote.zdsloupnice.cz/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Přidání kódu pro nastavení Gson na přísnější režim
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Vytvořte OkHttpClient s nastavením časového limitu a přidáním loggingInterceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS) // Nastavte časový limit pro připojení
                    .readTimeout(30, TimeUnit.SECONDS) // Nastavte časový limit pro čtení dat
                    .writeTimeout(30, TimeUnit.SECONDS) // Nastavte časový limit pro zápis dat
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
