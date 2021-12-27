package kitchenpos.order.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.util.HttpUtil;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.assertj.core.api.Assertions;

public class OrderAcceptanceStep {

    private static final String API_URL = "/api/orders";

    private OrderAcceptanceStep() {
    }

    public static ExtractableResponse<Response> 주문_등록_요청(OrderRequest order) {
        return HttpUtil.post(API_URL, order);
    }

    public static ExtractableResponse<Response> 주문_목록조회_요청() {
        return HttpUtil.get(API_URL);
    }

    public static ExtractableResponse<Response> 주문_상태변경_요청(Long orderId,
        OrderStatusRequest orderStatusRequest) {
        String url = API_URL + "/" + orderId + "/order-status";
        return HttpUtil.put(url, orderStatusRequest);
    }

    public static Long 주문_등록_검증(ExtractableResponse<Response> response) {
        OrderResponse 등록된_주문 = response.as(OrderResponse.class);

        assertThat(등록된_주문.getId()).isNotNull();
        return 등록된_주문.getId();
    }

    public static List<OrderResponse> 주문_목록조회_검증(ExtractableResponse<Response> response,
        Long expected) {
        List<OrderResponse> 조회된_주문_목록 = response.as(new TypeRef<List<OrderResponse>>() {
        });
        Assertions.assertThat(조회된_주문_목록).extracting("id").contains(expected);

        return 조회된_주문_목록;
    }


    public static void 주문_상태변경_검증(ExtractableResponse<Response> response, String expected) {
        OrderResponse 변경된_주문 = response.as(OrderResponse.class);
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(expected);
    }

}
