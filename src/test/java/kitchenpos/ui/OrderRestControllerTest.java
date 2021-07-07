package kitchenpos.ui;

import static kitchenpos.domain.OrderTableTest.*;
import static kitchenpos.domain.OrderTest.*;
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
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(테이블12_사용중_주문전.getId(), Arrays.asList(orderLineItem));
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
            .andExpect(jsonPath("$[0].id").value(테이블9주문.getId()))
            .andExpect(jsonPath("$[0].orderTableId").value(테이블9_사용중.getId())) // TODO 나중에 복구
            .andExpect(jsonPath("$[0].orderStatus").value(테이블9주문.getOrderStatus()));
    }

    @Test
    @Order(3)
    @DisplayName("특정 주문의 상태를 변경한다")
    void changeOrderStatus() throws Exception {
        OrderRequest request = new OrderRequest("COMPLETION");
        mockMvc.perform(put("/api/orders/{id}/order-status", 테이블10주문.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$.id").value(테이블10주문.getId()))
            .andExpect(jsonPath("$.orderTableId").value(테이블10_사용중.getId()))
            .andExpect(jsonPath("$.orderStatus").value(request.getOrderStatus()));
    }
}
