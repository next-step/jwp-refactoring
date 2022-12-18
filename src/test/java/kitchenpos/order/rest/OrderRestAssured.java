package kitchenpos.order.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.resource.UriResource;
import org.springframework.http.MediaType;

public class OrderRestAssured {

    public static OrderResponse 주문_등록됨(OrderCreateRequest request) {
            return 주문_등록_요청(request).as(OrderResponse.class);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(OrderCreateRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(UriResource.주문_API.uri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(UriResource.주문_API.uri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long id, OrderStatus orderStatus) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderUpdateRequest(orderStatus))
                .when().put(UriResource.주문_API.uri() + "/{id}/order-status", id)
                .then().log().all()
                .extract();
    }
}
