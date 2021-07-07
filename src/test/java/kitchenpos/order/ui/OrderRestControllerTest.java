package kitchenpos.order.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.IntegrationTestHelper;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.OrderRestController;
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
        Order order = new Order(1L, null, Arrays.asList(orderLineItem1, orderLineItem2));
        Mockito.when(orderService.create(any())).thenReturn(order);

        // when
        ResultActions resultActions = 주문_생성_요청(order);

        // then
        주문_생성_성공(resultActions);
    }

    @DisplayName("전체 주문 조회 요청")
    @Test
    void listTest() throws Exception {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order1 = new Order(1L, null, Arrays.asList(orderLineItem1, orderLineItem2));
        Order order2 = new Order(2L, null, Arrays.asList(orderLineItem1, orderLineItem2));
        Mockito.when(orderService.list()).thenReturn(Arrays.asList(order1, order2));

        // when
        ResultActions resultActions = 전체_주문_조회_요청();

        // then
        MvcResult mvcResult = 주문_조회_성공(resultActions);
        List<Order> orders = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Order>>(){});
        assertThat(orders).isNotEmpty().hasSize(2);
    }

    @DisplayName("주문 상태 변경 요청")
    @Test
    void changeOrderStatusTest() throws Exception {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order = new Order(1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2));
        Mockito.when(orderService.changeOrderStatus(any(), any())).thenReturn(order);

        // when
        ResultActions resultActions = 주문_상태_변경_요청(1L, order);

        // then
        주문_상태_변경_성공(resultActions);
    }

    private ResultActions 주문_생성_요청(final Order order) throws Exception {
        return postRequest("/api/orders", order);
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

    private ResultActions 주문_상태_변경_요청(final Long orderId, final Order order) throws Exception {
        String uri = String.format("/api/orders/%s/order-status", orderId);
        return putRequest(uri, order);
    }

    private MvcResult 주문_상태_변경_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk()).andReturn();
    }
}
