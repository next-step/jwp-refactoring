package kitchenpos.acceptance.order;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.menu.MenuAcceptanceTest;
import kitchenpos.acceptance.menugroup.MenuGroupAcceptanceTest;
import kitchenpos.acceptance.ordertable.OrderTableAcceptanceTest;
import kitchenpos.acceptance.product.ProductAcceptanceTest;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import kitchenpos.dto.product.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    private OrderTableResponse 주문테이블;
    private MenuGroupResponse 오늘의메뉴;
    private ProductResponse 후라이드;
    private ProductResponse 양념치킨;
    private MenuResponse 메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문테이블 = OrderTableAcceptanceTest.주문_테이블_등록_요청(0, false).as(OrderTableResponse.class);
        오늘의메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록_요청("오늘의메뉴").as(MenuGroupResponse.class);
        후라이드 = ProductAcceptanceTest.상품_등록_요청("후라이드", 15000).as(ProductResponse.class);
        양념치킨 = ProductAcceptanceTest.상품_등록_요청("양념치킨", 15000).as(ProductResponse.class);

        MenuProductRequest 메뉴상품1 = new MenuProductRequest(후라이드.getId(), 1);
        MenuProductRequest 메뉴상품2 = new MenuProductRequest(양념치킨.getId(), 1);
        메뉴 = MenuAcceptanceTest.메뉴_등록_요청("후양 두마리 세트", 28000, 오늘의메뉴, Arrays.asList(메뉴상품1, 메뉴상품2)).as(MenuResponse.class);
    }

    @Test
    @DisplayName("시나리오1: 주문을 등록하고 관리할 수 있다.")
    public void scenarioTest() throws Exception {
        // given
        Map<String, Object> menuRequest = new HashMap<>();
        menuRequest.put("menuId", 메뉴.getId());
        menuRequest.put("quantity", 1);

        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderTableId", 주문테이블.getId());
        orderRequest.put("orderLineItems", Collections.singletonList(menuRequest));

        // when 주문 등록 요청
        ExtractableResponse<Response> 주문_등록 = 주문_등록_요청(orderRequest);
        // then 주문 등록 요청됨
        주문_등록_요청됨(주문_등록);

        // when 주문 목록 조회 요청
        ExtractableResponse<Response> 주문_목록_조회 = 주문_목록_조회_요청();
        // then 주문 목록 조회됨
        주문_목록_조회됨(주문_목록_조회);

        // when 주문 상태 변경 요청
        ExtractableResponse<Response> 주문_상태_변경 = 주문_상태_변경_요청(주문_등록, OrderStatus.COOKING);
        // then 주문 상태 변경됨
        주문_상태_변경됨(주문_상태_변경);
    }

    @Test
    @DisplayName("시나리오2: 주문하려는 메뉴가 등록이 되어있지 않으면 주문할 수 없습니다.")
    public void scenarioTest2() throws Exception {
        // given
        Long unregisteredMenuId = 10L;
        Map<String, Object> menuRequest = new HashMap<>();
        menuRequest.put("menuId", unregisteredMenuId);
        menuRequest.put("quantity", 1);

        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderTableId", 주문테이블.getId());
        orderRequest.put("orderLineItems", Collections.singletonList(menuRequest));

        // when 주문 등록 요청
        ExtractableResponse<Response> 주문_등록 = 주문_등록_요청(orderRequest);
        // then 주문 등록 요청 실패됨
        주문_등록_실패됨(주문_등록);
    }

    @Test
    @DisplayName("시나리오3: 주문 수량이 0 이하 이면 주문할 수 없습니다.")
    public void scenarioTest3() throws Exception {
        // given
        Map<String, Object> menuRequest = new HashMap<>();
        menuRequest.put("menuId", 메뉴.getId());
        menuRequest.put("quantity", 0);

        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderTableId", 주문테이블.getId());
        orderRequest.put("orderLineItems", Collections.singletonList(menuRequest));

        // when 주문 등록 요청
        ExtractableResponse<Response> 주문_등록 = 주문_등록_요청(orderRequest);
        // then 주문 등록 요청 실패됨
        주문_등록_실패됨(주문_등록);
    }

    private ExtractableResponse<Response> 주문_등록_요청(Map<String, Object> orderRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_등록_요청됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<OrderResponse> orderResponses = response.jsonPath().getList(".", OrderResponse.class);
        OrderResponse addedOrderResponse = orderResponses.get(orderResponses.size() - 1);
        assertAll(
                () -> assertThat(addedOrderResponse.getId()).isNotNull(),
                () -> assertThat(addedOrderResponse.getOrderTableId()).isEqualTo(주문테이블.getId()),
                () -> assertThat(addedOrderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(addedOrderResponse.getOrderedTime()).isNotNull(),
                () -> assertThat(addedOrderResponse.getOrderLineItems()).hasSize(1),
                () -> assertThat(addedOrderResponse.getOrderLineItems()).hasSize(1)
                        .extracting(OrderLineItemResponse::getSeq).isNotNull(),
                () -> assertThat(addedOrderResponse.getOrderLineItems()).hasSize(1)
                        .extracting(OrderLineItemResponse::getOrderId).containsOnly(addedOrderResponse.getId()),
                () -> assertThat(addedOrderResponse.getOrderLineItems()).hasSize(1)
                        .extracting(OrderLineItemResponse::getMenuId).containsOnly(메뉴.getId()),
                () -> assertThat(addedOrderResponse.getOrderLineItems()).hasSize(1)
                        .extracting(OrderLineItemResponse::getQuantity).containsOnly(1L)
        );
    }

    private void 주문_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 주문_상태_변경_요청(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        String location = response.header("Location");
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(orderStatus.name());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderStatusRequest)
                .when().put(location + "/order-status")
                .then().log().all()
                .extract();
    }

    private void 주문_상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
