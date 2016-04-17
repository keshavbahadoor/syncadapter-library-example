package api;

import models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Keshav on 3/27/2016.
 */
public interface TestAPI {

    @GET("users/{userid}")
    Call<User> getUser(@Path("userid") int userid);

}
