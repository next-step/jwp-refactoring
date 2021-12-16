package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : OrderRestControllerTest
 * author : haedoang
 * date : 2021-12-15
 * description :
 */
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    private Order order;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setSeq(1L);
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(1);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setSeq(2L);
        orderLineItem2.setOrderId(2L);
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(2);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        order = new Order();
        order.setId(1L);
        order.setOrderStatus("COOKING");
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(2L);
    }

    @Test
    @DisplayName("주믄을 조회한다.")
    public void getOrders() throws Exception {
        // given
        List<Order> orders = Arrays.asList(order);
        given(orderService.list()).willReturn(orders);

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderStatus", is("COOKING")))
                .andExpect(jsonPath("$[0].orderLineItems", hasSize(2)))
                .andDo(print());
    }


    @Test
    @DisplayName("주문을 등록한다.")
    public void postOrder() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(orderService.create(any(Order.class))).willReturn(order);

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(order))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.orderStatus", is(order.getOrderStatus())))
                .andExpect(jsonPath("$.orderLineItems", hasSize(2)))
                .andDo(print());
    }


    @Test
    @DisplayName("주문상태를 변경한다.")
    public void putOrder() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(order);
        order.setOrderStatus("CHANGE_STATUS");

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/orders/" + order.getId() + "/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(order))
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus", is("CHANGE_STATUS")))
                .andExpect(jsonPath("$.orderLineItems", hasSize(2)))
                .andDo(print());
    }
}