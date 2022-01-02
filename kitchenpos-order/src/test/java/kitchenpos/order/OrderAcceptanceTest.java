package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.utils.RestTestApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    private static final String URI = "/api/orders";

    private OrderRequest 주문_후라이드;
    private OrderRequest 주문_양념치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();

        OrderLineItemRequest 주문항목_후라이드 = new OrderLineItemRequest(1L, 1L);
        OrderLineItemRequest 주문항목_양념치킨 = new OrderLineItemRequest(2L, 1L);

        주문_후라이드 = new OrderRequest(9L, OrderStatus.COOKING,
            Arrays.asList(주문항목_후라이드));
        주문_양념치킨 = new OrderRequest(10L, OrderStatus.COOKING,
            Arrays.asList(주문항목_양념치킨));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> 주문_생성_응답 = 주문_생성_요청(주문_후라이드);

        // then
        주문_생성됨(주문_생성_응답);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 주문_후라이드_응답 = 주문_생성_요청(주문_후라이드);
        ExtractableResponse<Response> 주문_양념치킨_응답 = 주문_생성_요청(주문_양념치킨);

        // when
        ExtractableResponse<Response> 주문_목록_응답 = 주문_목록_요청();

        // then
        주문_목록_응답됨(주문_목록_응답);
        주문_목록_포함됨(주문_목록_응답, Arrays.asList(주문_후라이드_응답, 주문_양념치킨_응답));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderResponse 주문_후라이드_응답 = 주문_생성_요청(주문_후라이드).as(OrderResponse.class);

        // when
        OrderRequest 주문_상태변경 = new OrderRequest(OrderStatus.MEAL);
        ExtractableResponse<Response> response = 주문_상태변경_요청(주문_후라이드_응답.getId(), 주문_상태변경);

        // then
        주문_상태_변경됨(response);
    }

    @DisplayName("등록되지 않은 주문은 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusNotRegistered() {
        //when
        OrderRequest 주문_상태변경 = new OrderRequest(OrderStatus.MEAL);
        Long 등록안된_주문Id = 0L;
        ExtractableResponse<Response> response = 주문_상태변경_요청(등록안된_주문Id, 주문_상태변경);

        // then
        주문상태_변경_실패됨(response);
    }

    @DisplayName("계산 완료된 주문은 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusCompleted() {
        // given
        OrderResponse 주문_후라이드_응답 = 주문_생성_요청(주문_후라이드).as(OrderResponse.class);
        OrderRequest 주문_완료상태_변경 = new OrderRequest(OrderStatus.COMPLETION);
        주문_상태변경_요청(주문_후라이드_응답.getId(), 주문_완료상태_변경);

        // when
        OrderRequest 주문_다시상태변경 = new OrderRequest(OrderStatus.COOKING);
        ExtractableResponse<Response> response = 주문_상태변경_요청(주문_후라이드_응답.getId(), 주문_다시상태변경);

        // then
        주문상태_변경_실패됨(response);
    }

    private void 주문상태_변경_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 주문_상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 주문_상태변경_요청(Long orderId, OrderRequest orderRequest) {
        String uri = URI + String.format("/%d/order-status", orderId);
        return RestTestApi.put(uri, orderRequest);
    }

    private static ExtractableResponse<Response> 주문_생성_요청(OrderRequest request) {
        return RestTestApi.post(URI, request);
    }

    private static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 주문_목록_요청() {
        return RestTestApi.get(URI);
    }

    private void 주문_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문_목록_포함됨(ExtractableResponse<Response> response,
        List<ExtractableResponse<Response>> expectedResponses) {

        List<Long> responseIds = response.jsonPath().getList(".", OrderResponse.class).stream()
            .map(OrderResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedIds = expectedResponses.stream()
            .map(expectedResponse -> expectedResponse.as(OrderResponse.class))
            .map(OrderResponse::getId)
            .collect(Collectors.toList());

        assertThat(responseIds).containsAll(expectedIds);
    }
}
