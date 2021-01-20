package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import kitchenpos.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관리")
public class OrderAcceptanceTest extends AcceptanceTest {
    @DisplayName("주문을 관리한다")
    @Test
    void manage() {
        OrderResponse order = 주문_생성();
        주문_조회();
        주문_상태_변경(order);
    }

    private OrderResponse 주문_생성() {
        MenuGroupResponse menuGroup = MenuGroupAcceptanceTest.생성_요청(MenuGroupAcceptanceTest.createRequest())
                .as(MenuGroupResponse.class);
        ProductResponse product = ProductAcceptanceTest.생성_요청(ProductAcceptanceTest.createRequest())
                .as(ProductResponse.class);
        MenuResponse menu = MenuAcceptanceTest.생성_요청(MenuAcceptanceTest.createRequest(menuGroup, product))
                .as(MenuResponse.class);
        TableResponse orderTable = TableAcceptanceTest.생성_요청()
                .as(TableResponse.class);

        OrderRequest request = createRequest(orderTable, menu);
        ExtractableResponse<Response> createdResponse = 생성_요청(request);

        생성됨(createdResponse, request);
        return createdResponse.as(OrderResponse.class);
    }

    private void 주문_조회() {
        ExtractableResponse<Response> selectedResponse = 조회_요청();

        조회됨(selectedResponse);
    }

    private void 주문_상태_변경(OrderResponse order) {
        ExtractableResponse<Response> updatedResponse = 상태_변경_요청(order, OrderStatus.MEAL);

        상태_변경됨(updatedResponse, OrderStatus.MEAL);
    }

    public static OrderRequest createRequest(TableResponse orderTable, MenuResponse menu) {
        List<OrderMenuRequest> orderMenuRequests = Collections.singletonList(new OrderMenuRequest(menu.getId(), 1L));
        return new OrderRequest(orderTable.getId(), orderMenuRequests);
    }

    public static ExtractableResponse<Response> 생성_요청(OrderRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static void 생성됨(ExtractableResponse<Response> response, OrderRequest request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        OrderResponse order = response.as(OrderResponse.class);
        assertThat(order.getOrderTableId()).isEqualTo(request.getOrderTableId());
    }

    public static ExtractableResponse<Response> 조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static void 조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<OrderResponse> orders = Arrays.asList(response.as(OrderResponse[].class));
        assertThat(orders.size()).isEqualTo(1);
    }

    public static ExtractableResponse<Response> 상태_변경_요청(OrderResponse request, OrderStatus orderStatus) {
        return RestAssured
                .given().log().all()
                .param("orderStatus", orderStatus)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/{orderId}", request.getId())
                .then().log().all()
                .extract();
    }

    public static void 상태_변경됨(ExtractableResponse<Response> response, OrderStatus requestStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        OrderResponse order = response.as(OrderResponse.class);
        assertThat(order.getOrderStatus()).isEqualTo(requestStatus.name());
    }
}
