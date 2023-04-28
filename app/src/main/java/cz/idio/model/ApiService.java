package cz.idio.model;



import cz.idio.api.response.CantResponse;
import cz.idio.api.response.LoginResponse;
import cz.idio.api.response.WorklistResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api.php")
    @FormUrlEncoded

    Call<LoginResponse> login(@Field("Mod")String mod,
                              @Field("Cmd")String cmd,
                              @Field("Login") String login,
                              @Field("Pwd") String pwd);

    @POST("api.php")
    @FormUrlEncoded
    Call<CantResponse> getCantData(
            @Field("Mod") String mod,
            @Field("Cmd") String cmd,
            @Field("CompLogin") String compLogin,
            @Field("Login") String login,
            @Field("Pwd") String pwd,
            @Field("FirstDate") String firstDate
    );
    @POST("api.php")
    @FormUrlEncoded
    Call<CantResponse> getCantDataMenu(
            @Field("Mod") String mod,
            @Field("Cmd") String cmd,
            @Field("CompLogin") String compLogin,
            @Field("Login") String login,
            @Field("Pwd") String pwd,
            @Field("FirstDate") String firstDate,
            @Field("CantMenuDay_Id") int CantMenuDay_Id
    );
    @POST("api.php")
    @FormUrlEncoded
    Call<CantResponse> getAddDelMenu(
            @Field("Mod") String mod,
            @Field("Cmd") String cmd,
            @Field("CompLogin") String compLogin,
            @Field("Login") String login,
            @Field("Pwd") String pwd,
            @Field("CantMenu_Id") String CantMenu_Id,
            @Field("Pos") String Pos
    );

    @POST("api.php")
    @FormUrlEncoded
    Call<WorklistResponse> getWorklist(@Field("Mod")String mod,
                                       @Field("Cmd")String cmd,
                                       @Field("CompLogin") String compLogin,
                                       @Field("Login") String login,
                                       @Field("Pwd") String pwd,
                                       @Field("Person_Id") int  person_Id,
                                       @Field("Date")String date);

}