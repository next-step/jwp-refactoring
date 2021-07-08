package kitchenpos.order.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.IntegrationTestHelper;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends IntegrationTestHelper {

    @MockBean
    private OrderService orderService;

    @Autowired
    private OrderRestController orderRestController;


    @Override
    protected Object controller() {
        return orderRestController;
    }

    @DisplayName("주문 생성 요청")
    @Test
    void createTest() throws Exception {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order = new Order(1l, OrderStatus.COOKING.name());
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 3);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

        OrderResponse orderResponse = OrderResponse.of(order);
        Mockito.when(orderService.create(any())).thenReturn(orderResponse);

        // when
        ResultActions resultActions = 주문_생성_요청(orderRequest);

        // then
        주문_생성_성공(resultActions);
    }

    @DisplayName("전체 주문 조회 요청")
    @Test
    void listTest() throws Exception {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order1 = new Order(1l, OrderStatus.COOKING.name());
        order1.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        Order order2 = new Order(2l, OrderStatus.COOKING.name());
        order2.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        Mockito.when(orderService.list()).thenReturn(Arrays.asList(OrderResponse.of(order1),
                                                                   OrderResponse.of(order2)));

        // when
        ResultActions resultActions = 전체_주문_조회_요청();

        // then
        MvcResult mvcResult = 주문_조회_성공(resultActions);
        List<OrderResponse> orderResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                                    new TypeReference<List<OrderResponse>>(){});
        assertThat(orderResponses).isNotEmpty().hasSize(2);
    }

    @DisplayName("주문 상태 변경 요청")
    @Test
    void changeOrderStatusTest() throws Exception {
        // given
        Mockito.when(orderService.changeOrderStatus(any(), any())).thenReturn(new OrderResponse());

        // when
        ResultActions resultActions = 주문_상태_변경_요청(1L, new OrderStatusRequest(OrderStatus.COOKING));

        // then
        주문_상태_변경_성공(resultActions);
    }

    private ResultActions 주문_생성_요청(final OrderRequest orderRequest) throws Exception {
        return postRequest("/api/orders", orderRequest);
    }

    private MvcResult 주문_생성_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isCreated()).andReturn();
    }

    private ResultActions 전체_주문_조회_요청() throws Exception {
        return getRequest("/api/orders");
    }

    private MvcResult 주문_조회_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk()).andReturn();
    }

    private ResultActions 주문_상태_변경_요청(final Long orderId, final OrderStatusRequest orderStatusRequest) throws Exception {
        String uri = String.format("/api/orders/%s/order-status", orderId);
        return putRequest(uri, orderStatusRequest);
    }

    private MvcResult 주문_상태_변경_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk()).andReturn();
    }
}
