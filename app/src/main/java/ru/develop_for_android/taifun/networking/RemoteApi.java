package ru.develop_for_android.taifun.networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.develop_for_android.taifun.data.OrderEntry;

public interface RemoteApi {

    @POST("/post_order.json")
    Call<Long> sendOrder(OrderEntry orderEntry);

    @GET("get_order_status.json")
    Call<Integer> getOrderStatus(long remoteOrderId);
}
