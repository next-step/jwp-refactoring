package kitchenpos.ui;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableRequest;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = BEFORE_CLASS)
@DisplayName("주문 통합 테스트")
class OrderRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("주문을 생성한다")
    void create() throws Exception {
        Long orderTableId = 1L;

        OrderTableRequest orderTableRequest = new OrderTableRequest(false);
        mockMvc.perform(put("/api/tables/{id}/empty", orderTableId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderTableRequest)));

        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(orderTableId, Arrays.asList(orderLineItem));
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @DisplayName("주문 목록을 가져온다")
    void list() throws Exception {
        mockMvc.perform(get("/api/orders"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].orderTableId").value(1))
            .andExpect(jsonPath("$[0].orderStatus").value("COOKING"));
    }

    @Test
    @Order(3)
    @DisplayName("특정 주문의 상태를 변경한다")
    void changeOrderStatus() throws Exception {
        OrderRequest request = new OrderRequest("MEAL");
        mockMvc.perform(put("/api/orders/{id}/order-status", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.orderTableId").value(1))
            .andExpect(jsonPath("$.orderStatus").value("MEAL"));
    }
}
