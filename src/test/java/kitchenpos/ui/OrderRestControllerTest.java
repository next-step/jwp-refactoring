package kitchenpos.ui;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;

class OrderRestControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("주문을 등록한다")
    @Test
    Long 주문_등록() throws Exception {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 1L), new OrderLineItem(2L, 1L));
        Order order = new Order(9L, orderLineItems);

        MvcResult result = mockMvc.perform(post("/api/orders")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(order)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.orderTableId").value(order.getOrderTable().getId()))
            .andExpect(jsonPath("$.orderStatus").value(COOKING.name()))
            .andExpect(jsonPath("$.orderedTime").exists())
            .andExpect(jsonPath("$.orderLineItems.length()").value(orderLineItems.size()))
            .andReturn();

        Long id = getId(result);
        assertThat(orderRepository.findById(id)).isNotEmpty();
        return id;
    }

    @DisplayName("전체 주문을 조회한다")
    @Test
    void order2() throws Exception {
        주문_등록();

        mockMvc.perform(get("/api/orders"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..id").exists())
            .andExpect(jsonPath("$..orderTableId").exists())
            .andExpect(jsonPath("$..orderStatus").exists())
            .andExpect(jsonPath("$..orderedTime").exists())
            .andExpect(jsonPath("$..orderLineItems").exists())
        ;
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void order3() throws Exception {
        Long id = 주문_등록();
        Order order = new Order(MEAL);

        mockMvc.perform(put("/api/orders/" + id + "/order-status")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(order)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.orderTableId").exists())
            .andExpect(jsonPath("$.orderStatus").value(order.getOrderStatus().name()))
            .andExpect(jsonPath("$.orderedTime").exists());

        assertThat(orderRepository.findById(id)).isNotEmpty();
    }

    private Long getId(MvcResult result) throws
        com.fasterxml.jackson.core.JsonProcessingException,
        UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, Order.class).getId();
    }

}
