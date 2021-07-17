package kitchenpos.order.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.common.domain.OrderStatus;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("주문 관리 기능")
@WebMvcTest(controllers = OrderRestController.class)
class OrderRestControllerTest extends MockMvcControllerTest {

    public static final String DEFAULT_REQUEST_URL = "/api/orders";
    @MockBean
    private OrderService orderService;

    @Autowired
    private OrderRestController orderRestController;

    @Override
    protected Object controller() {
        return orderRestController;
    }

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create_order() throws Exception {
        // given
        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(OrderStatus.COOKING, 1L, Arrays.asList(orderLineItemRequest1));
        Order order = new Order(LocalDateTime.now(), 1L);
        order.addOrderLineItem(new OrderLineItem(order, 1L, 3L));
        OrderResponse orderResponse = OrderResponse.of(order);
        given(orderService.create(any(OrderRequest.class))).willReturn(orderResponse);

        // then
        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("orderStatus").value(orderResponse.getOrderStatus().name()))
                .andExpect(jsonPath("orderLineItemResponses.length()").value(1))
        ;
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다.")
    void retrieve_orderList() throws Exception {
        // given
        Order order = new Order(LocalDateTime.now(), 1L);
        order.addOrderLineItem(new OrderLineItem(order, 1L, 3L));
        OrderResponse orderResponse = OrderResponse.of(order);
        given(orderService.findAllOrders()).willReturn(Arrays.asList(orderResponse));

        // then
        mockMvc.perform(get(DEFAULT_REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].orderStatus").value(orderResponse.getOrderStatus().name()))
                .andExpect(jsonPath("length()").value(1))
        ;
    }

    @Test
    @DisplayName("주문의 상태를 수정할 수 있다.")
    void change_orderStatus() throws Exception {
        // given
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL, 1L, new ArrayList<>());
        Order order = new Order(LocalDateTime.now(), 1L);
        order.changeOrderStatus(OrderStatus.MEAL);
        order.addOrderLineItem(new OrderLineItem(order, 1L, 3L));
        OrderResponse orderResponse = OrderResponse.of(order);
        given(orderService.changeOrderStatus(anyLong(), any(OrderRequest.class))).willReturn(orderResponse);

        // then
        mockMvc.perform(put(DEFAULT_REQUEST_URL + "/1/order-status")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(orderRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus").value(orderResponse.getOrderStatus().name()))
        ;
    }
}
