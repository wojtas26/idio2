package cz.idio.shift;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AdminService {
    @POST("createShift")
    Call<Void> createShift(@Body Shift shift);

    @POST("approveRequest")
    Call<Void> approveRequest(@Body Request request);

    @POST("rejectRequest")
    Call<Void> rejectRequest(@Body Request request);
}

