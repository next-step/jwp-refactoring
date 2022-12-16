package kitchenpos.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.resource.UriResource;
import org.springframework.http.MediaType;

import java.util.List;

public class OrderRestAssured {

    public static ExtractableResponse<Response> 주문_등록됨(long orderTableId, List<OrderLineItem> orderLineItems) {
            return 주문_등록_요청(orderTableId, orderLineItems);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(long orderTableId, List<OrderLineItem> orderLineItems) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new Order(orderTableId, orderLineItems))
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
                .body(new Order(orderStatus))
                .when().put(UriResource.주문_API.uri() + "/{id}/order-status", id)
                .then().log().all()
                .extract();
    }
}
