package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
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
        //given
        MenuGroup menuGroup = MenuGroupAcceptanceTest.생성_요청(MenuGroupAcceptanceTest.createRequest())
                .as(MenuGroup.class);
        Product product = ProductAcceptanceTest.생성_요청(ProductAcceptanceTest.createRequest())
                .as(Product.class);
        Menu menu = MenuAcceptanceTest.생성_요청(MenuAcceptanceTest.createRequest(menuGroup, product))
                .as(Menu.class);
        OrderTable orderTable = TableAcceptanceTest.생성_요청()
                .as(OrderTable.class);
        //when
        Order request = createRequest(orderTable, menu);
        ExtractableResponse<Response> createdResponse = 생성_요청(request);
        //then
        생성됨(createdResponse, request);
        //when
        ExtractableResponse<Response> selectedResponse = 조회_요청();
        //then
        조회됨(selectedResponse);
        //when
        Order order = createdResponse.as(Order.class);
        order.setOrderStatus(OrderStatus.MEAL.name());
        ExtractableResponse<Response> updatedResponse = 상태_변경_요청(order);
        //then
        상태_변경됨(updatedResponse, order);
    }

    public static Order createRequest(OrderTable orderTable, Menu menu) {
        Order request = new Order();
        request.setOrderTableId(orderTable.getId());
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);
        request.setOrderLineItems(Collections.singletonList(orderLineItem));
        return request;
    }

    public static ExtractableResponse<Response> 생성_요청(Order request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static void 생성됨(ExtractableResponse<Response> response, Order request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Order order = response.as(Order.class);
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
        List<Order> orders = Arrays.asList(response.as(Order[].class));
        assertThat(orders.size()).isEqualTo(1);
    }

    public static ExtractableResponse<Response> 상태_변경_요청(Order request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/{orderId}/order-status", request.getId())
                .then().log().all()
                .extract();
    }

    public static void 상태_변경됨(ExtractableResponse<Response> response, Order request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Order order = response.as(Order.class);
        assertThat(order.getOrderStatus()).isEqualTo(request.getOrderStatus());
    }
}
