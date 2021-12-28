package kitchenpos.order;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.TableFactory;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("주문 테스트")
 public class OrderServiceAcceptanceTest extends AcceptanceTest {

    OrderTableResponse createdOrderTable;
    List<OrderLineItemRequest> orderLineItems;

    @BeforeEach
    public void setUp() {
        super.setUp();
        OrderLineItemRequest orderLineItem1 = new OrderLineItemRequest(1L, 1L);
        OrderLineItemRequest orderLineItem2 = new OrderLineItemRequest(2L, 1L);

        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);
        orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem1);
        orderLineItems.add(orderLineItem2);

        ExtractableResponse<Response> createResponse = TableFactory.주문테이블_생성_요청(orderTableRequest);
        createdOrderTable = 주문테이블이_생성됨(createResponse);

    }

    @DisplayName("주문을 등록한다")
    @Test
    void createTest() {

        OrderRequest orderRequest = new OrderRequest(createdOrderTable.getId(), null, orderLineItems);

        ExtractableResponse<Response> createOrderResponse = OrderFactory.주문_생성_요청(orderRequest);

        assertThat(createOrderResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("주문을 조회한다")
    @Test
    void getListTest() {

        OrderRequest orderRequest = new OrderRequest(createdOrderTable.getId(), null, orderLineItems);

        OrderResponse createdOrder = OrderFactory.주문_생성_요청(orderRequest).as(OrderResponse.class);

        ExtractableResponse<Response> response = OrderFactory.주문_조회_요청();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @DisplayName("주문을 변경한다")
    @Test
    void changeOrderStatusTest() {

        OrderRequest orderRequest = new OrderRequest(createdOrderTable.getId(), null, orderLineItems);

        OrderResponse createdOrder = OrderFactory.주문_생성_요청(orderRequest).as(OrderResponse.class);

        ExtractableResponse<Response> response = OrderFactory.주문_상태변경_요청(new OrderRequest(createdOrder.getOrderTableId(), OrderStatus.MEAL, orderLineItems), createdOrder.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    public static OrderTableResponse 주문테이블이_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(OrderTableResponse.class);
    }


}
