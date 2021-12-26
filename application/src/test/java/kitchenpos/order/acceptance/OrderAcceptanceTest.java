package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.acceptance.MenuAcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.acceptance.TableAcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;
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
        MenuResponse 대표메뉴 = MenuAcceptanceTest.대표메뉴_등록되어_있음();
        OrderTableResponse 일번_테이블 = TableAcceptanceTest.주문_테이블_등록되어_있음(4, false);
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(new OrderLineItemRequest(대표메뉴.getId(), 2));
        OrderRequest 주문 = new OrderRequest(일번_테이블.getId(), orderLineItems);

        // when
        ExtractableResponse<Response> createResponse = 주문_생성_요청(주문);
        // then
        OrderResponse 생성된_주문 = 주문_생성됨(createResponse);

        // when
        ExtractableResponse<Response> changeResponse = 주문상태_변경_요청(생성된_주문, OrderStatus.COMPLETION);
        // then
        OrderResponse 수정된_주문 = 주문상태_수정됨(changeResponse);

        // when
        ExtractableResponse<Response> listResponse = 모든_주문_조회_요청();
        // then
        모든_주문_조회_응답됨(listResponse);
        주문_목록에_포함됨(listResponse, 수정된_주문);
    }

    /**
     * 요청 관련
     */
    private static ExtractableResponse<Response> 주문_생성_요청(OrderRequest order) {
        return Http.post("/api/orders", order);
    }

    private static ExtractableResponse<Response> 주문상태_변경_요청(OrderResponse order, OrderStatus orderStatus) {
        return Http.put("/api/orders/" + order.getId() + "/order-status", new OrderRequest(orderStatus));
    }

    private static ExtractableResponse<Response> 모든_주문_조회_요청() {
        return Http.get("/api/orders");
    }

    /**
     * 응답 관련
     */
    private static OrderResponse 주문_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return createResponse.as(OrderResponse.class);
    }

    private static OrderResponse 주문상태_수정됨(ExtractableResponse<Response> changeResponse) {
        assertThat(changeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        return changeResponse.as(OrderResponse.class);
    }

    private static void 주문_목록에_포함됨(ExtractableResponse<Response> listResponse, OrderResponse order) {
        List<OrderResponse> orders = listResponse.jsonPath().getList(".", OrderResponse.class);
        assertThat(orders).contains(order);
    }

    private static void 모든_주문_조회_응답됨(ExtractableResponse<Response> listResponse) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * 테스트 픽스처 관련
     */
    public static OrderResponse 주문_생성_되어_있음(OrderTableResponse orderTable) {
        MenuResponse 대표메뉴 = MenuAcceptanceTest.대표메뉴_등록되어_있음();
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(new OrderLineItemRequest(대표메뉴.getId(), 2));
        OrderRequest order = new OrderRequest(orderTable.getId(), orderLineItems);
        return 주문_생성_요청(order).as(OrderResponse.class);
    }
}
