package kitchenpos.order.application;

import kitchenpos.order.dto.OrderMenuResponse;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface OrderMenuService {

    @GET("/api/menu-ids")
    Call<List<OrderMenuResponse>> findAllByIds(List<Long> ids);
}
