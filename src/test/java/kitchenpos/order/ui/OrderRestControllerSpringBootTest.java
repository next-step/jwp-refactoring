package kitchenpos.order.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("주문 관리 기능 - SpringBootTest")
@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class OrderRestControllerSpringBootTest extends MockMvcControllerTest {
    public static final String DEFAULT_REQUEST_URL = "/api/orders";
    @Autowired
    private OrderRestController orderRestController;

    @Override
    protected Object controller() {
        return orderRestController;
    }

    @Order(1)
    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create_order() throws Exception {
        // given
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(OrderStatus.COOKING, 1L, Arrays.asList(orderLineItemRequest));

        // then
        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("orderStatus").value(orderRequest.getOrderStatus().name()))
        ;
    }

    @Order(2)
    @Test
    @DisplayName("주문 목록을 조회할 수 있다.")
    void retrieve_orderList() throws Exception {
        // then
        mockMvc.perform(get(DEFAULT_REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(1))
                .andExpect(jsonPath("[0].orderTableId").value(1))
        ;
    }

    @Order(3)
    @Test
    @DisplayName("주문의 상태를 수정할 수 있다.")
    void change_orderStatus() throws Exception {
        // given
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL, 1L, new ArrayList<>());

        // then
        mockMvc.perform(put(DEFAULT_REQUEST_URL + "/1/order-status")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(orderRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus").value(orderRequest.getOrderStatus().name()))
        ;
    }
}
