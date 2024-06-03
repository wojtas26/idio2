package cz.idio.shift;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmployeeService {
    @POST("confirmShift")
    Call<Void> confirmShift(@Body Shift shift);

    @POST("rejectShift")
    Call<Void> rejectShift(@Body Shift shift);

    @POST("createRequest")
    Call<Void> createRequest(@Body Request request);

    Call<List<Shift>> getShifts();
}

