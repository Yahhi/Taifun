package ru.develop_for_android.taifun.networking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.develop_for_android.taifun.data.OrderWithFood;

public interface RemoteApi {

    @POST("post_order/")
    Call<Long> sendOrder(@Body OrderWithFood orderEntry);

    @POST("get_order_status/")
    Call<Integer> getOrderStatus(@Body CheckStatus request);
}
