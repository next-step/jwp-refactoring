package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import kitchenpos.utils.Http;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관리 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    @DisplayName("주문을 관리한다")
    @Test
    void testManagement() {
        // given
        Menu 대표메뉴 = MenuAcceptanceTest.대표메뉴_등록되어_있음();
        OrderTable 일번_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, false);
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(대표메뉴.getId(), 2));
        Order 주문 = new Order(일번_테이블.getId(), orderLineItems);

        // when
        ExtractableResponse<Response> createResponse = 주문_생성_요청(주문);
        // then
        Order 생성된_주문 = 주문_생성됨(createResponse);

        // when
        ExtractableResponse<Response> changeResponse = 주문상태_변경_요청(생성된_주문, OrderStatus.COMPLETION);
        // then
        Order 수정된_주문 = 주문상태_수정됨(changeResponse);

        // when
        ExtractableResponse<Response> listResponse = 모든_주문_조회_요청();
        // then
        모든_주문_조회_응답됨(listResponse);
        주문_목록에_포함됨(listResponse, 수정된_주문);
    }

    /**
     * 요청 관련
     */
    private ExtractableResponse<Response> 주문_생성_요청(Order order) {
        return Http.post("/api/orders", order);
    }

    private ExtractableResponse<Response> 주문상태_변경_요청(Order order, OrderStatus orderStatus) {
        return Http.put("/api/orders/" + order.getId() + "/order-status", new Order(orderStatus));
    }

    private ExtractableResponse<Response> 모든_주문_조회_요청() {
        return Http.get("/api/orders");
    }

    /**
     * 응답 관련
     */
    private Order 주문_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return createResponse.as(Order.class);
    }

    private Order 주문상태_수정됨(ExtractableResponse<Response> changeResponse) {
        assertThat(changeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        return changeResponse.as(Order.class);
    }

    private void 주문_목록에_포함됨(ExtractableResponse<Response> listResponse, Order order) {
        List<Order> orders = listResponse.jsonPath().getList(".", Order.class);
        assertThat(orders).contains(order);
    }

    private void 모든_주문_조회_응답됨(ExtractableResponse<Response> listResponse) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
