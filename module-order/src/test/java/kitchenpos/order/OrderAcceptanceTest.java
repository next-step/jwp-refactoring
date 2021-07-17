package kitchenpos.order;

import static kitchenpos.order.ui.OrderRestControllerTest.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptancePerMethodTest {

    public static final int 두명 = 2;
    public static final boolean 비어있지않음 = false;
    public static final boolean 비어있음 = true;

    @DisplayName("주문 관리")
    @Test
    void manage() {
        // Given
        Long 메뉴_ID = 1L;
        Long 주문테이블_ID = 1L;
        List<OrderLineItemRequest> 주문상품목록 = new ArrayList<>(Arrays.asList(new OrderLineItemRequest(메뉴_ID, 1)));
        OrderRequest 주문 = new OrderRequest(주문테이블_ID, OrderStatus.COOKING, LocalDateTime.now(), 주문상품목록);

        // When
        ExtractableResponse<Response> 주문_등록_응답 = 주문_등록_요청(주문);
        // Then
        Long 주문_ID = 주문이_등록됨(주문_등록_응답);

        // When
        ExtractableResponse<Response> 목록_조회_응답 = 주문_목록_조회_요청();
        // Then
        주문_목록_조회됨(목록_조회_응답);

        // When
        ExtractableResponse<Response> 주문상태_변경_응답 = 주문상태_변경_요청(주문_ID, OrderStatus.MEAL);
        // Then
        주문상태_변경됨(주문상태_변경_응답);
    }

    private ExtractableResponse<Response> 주문상태_변경_요청(Long id, OrderStatus orderStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderStatus", orderStatus);

        return put(params, BASE_URL + "/" + id + "/order-status");
    }

    public static void 주문상태_변경됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return get(BASE_URL);
    }

    private List<OrderResponse> 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<OrderResponse> 목록_조회_응답 = new ArrayList<>(response.jsonPath().getList(".", OrderResponse.class));
        assertThat(목록_조회_응답).hasSize(1);
        return 목록_조회_응답;
    }

    public static ExtractableResponse<Response> 주문_등록_요청(OrderRequest orderRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", orderRequest.getOrderTableId());
        params.put("orderLineItems", orderRequest.getOrderLineItems());

        return post(params, BASE_URL);
    }

    public static Long 주문이_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        return Long.parseLong(response.header("Location").split(BASE_URL + "/")[1]);
    }
}
