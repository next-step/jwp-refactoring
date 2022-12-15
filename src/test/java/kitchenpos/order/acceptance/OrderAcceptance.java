package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderAcceptance {


/*    @DisplayName("등록되어 있지 않은 주문 테이블에서 주문을 하면 실패")
    @Test
    void createOrderWithNullOrderTable() {
       // ExtractableResponse<Response> response = 주문_생성을_요청(-1L, Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)));

       // assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("등록되지 않은 메뉴를 포함해 주문하면 실패")
    @Test
    void createOrderWithNullMenu() {
        ExtractableResponse<Response> response = 주문_생성을_요청(-1L, Arrays.asList(new OrderLineItemRequest(-1L, 1L)));

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("빈 주문 테이블에서 주문한다.")
    @Test
    void createOrderWithEmptyOrderTable() {
        // when
        ExtractableResponse<Response> response = 주문_생성을_요청(빈_주문테이블.getId(), Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)));

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }


    public static ExtractableResponse<Response> 주문_생성을_요청(Long orderTableId,
                                                             List<OrderLineItemRequest> orderLineItems) {
        OrderRequest request = new OrderRequest(orderTableId, OrderStatus.COOKING, orderLineItems);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }*/


}
