package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_등록_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTest.주문테이블_등록_요청;
import static kitchenpos.fixture.MenuProductFactory.createMenuProductRequest;
import static kitchenpos.fixture.OrderFactory.createOrderLineItemRequest;
import static kitchenpos.fixture.OrderFactory.createOrderRequest;
import static kitchenpos.fixture.OrderFactory.createOrderStatusRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    private OrderTableResponse 주문테이블;
    private MenuGroupResponse 빅맥세트;
    private ProductResponse 토마토;
    private ProductResponse 양상추;
    private MenuResponse 빅맥버거;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문테이블 = 주문테이블_등록_요청(5, false).as(OrderTableResponse.class);
        빅맥세트 = 메뉴그룹_등록_요청("빅맥세트").as(MenuGroupResponse.class);
        토마토 = 상품_등록_요청("토마토", 1000).as(ProductResponse.class);
        양상추 = 상품_등록_요청("양상추", 500).as(ProductResponse.class);
        빅맥버거 = 메뉴_등록_요청("빅맥버거", BigDecimal.valueOf(3000), 빅맥세트.getId(),
                Arrays.asList(createMenuProductRequest(토마토.getId(), 1),
                        createMenuProductRequest(양상추.getId(), 4))).as(MenuResponse.class);
    }

    /**
     * Feature: 주문들 관리한다.
     * <p>
     * Scenario: 주문 관리
     * <p>
     * When: 주문 등록 요청
     * <p>
     * Then: 주문이 등록됨
     * <p>
     * When: 주문 목록 조회 요청
     * <p>
     * Then: 주문 목록이 조회됨
     * <p>
     * When: 주문 상태 변경 요청
     * <p>
     * Then: 주문 상태가 변경됨
     */
    @Test
    void 주문_관리() {
        ExtractableResponse<Response> response;
        // when 주문 등록 요청
        response = 주문_등록_요청(주문테이블.getId(), Arrays.asList(createOrderLineItemRequest(빅맥버거.getId(), 1)));
        // then 주문 등록됨
        주문_등록됨(response);
        OrderResponse 주문 = response.as(OrderResponse.class);

        // when 주문 목록 조회 요청
        response = 주문_목록_조회();
        // then 주문 목록이 조회됨
        주문_목록_조회됨(response);
        // then 주문 목록이 포함됨
        주문_목록_포함됨(response, Arrays.asList(주문));

        // when 주문 상태를 완료로 변경
        response = 주문상태_변경_요청(주문, OrderStatus.COMPLETION);
        // then 주문 상태가 완료로 변경됨
        주문상태_변경됨(response, OrderStatus.COMPLETION);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(Long orderTableId,
                                                         List<OrderLineItemRequest> orderLineItemRequests) {
        OrderRequest orderRequest = createOrderRequest(orderTableId, orderLineItemRequests);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문상태_변경_요청(OrderResponse order, OrderStatus orderStatus) {
        OrderStatusRequest request = createOrderStatusRequest(orderStatus);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/api/orders/{orderId}/order-status", order.getId())
                .then().log().all()
                .extract();
    }

    public static void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> response, List<OrderResponse> expectedOrders) {
        List<Long> resultOrderIds = response.jsonPath().getList(".", OrderResponse.class).stream()
                .map(OrderResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedOrderIds = expectedOrders.stream()
                .map(OrderResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultOrderIds).containsAll(expectedOrderIds);
    }

    public static void 주문상태_변경됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo(orderStatus.name());
    }
}
