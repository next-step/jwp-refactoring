package kitchenpos.ui;

import static kitchenpos.domain.MenuProductTest.*;
import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.domain.OrderTableTest.*;
import static kitchenpos.domain.OrderTest.*;
import static kitchenpos.domain.ProductTest.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
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

import kitchenpos.dto.OrderLineItemDetailRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;

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
        OrderLineItemDetailRequest detailRequest = new OrderLineItemDetailRequest(MP1후라이드.getSeq(), 후라이드.getId(), "후라이드", BigDecimal.valueOf(16000), 1);
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L, 후라이드_메뉴.getName(), 후라이드_메뉴.getPrice().value(), 1, Arrays.asList(detailRequest));
        OrderRequest request = new OrderRequest(테이블12_사용중_주문전.getId(), Arrays.asList(orderLineItem));
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.orderTableId").value(테이블12_사용중_주문전.getId()))
            .andExpect(jsonPath("$.orderStatus").value(COOKING.name()));
    }

    @Test
    @Order(2)
    @DisplayName("주문 목록을 가져온다")
    void list() throws Exception {
        mockMvc.perform(get("/api/orders"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(테이블9주문.getId()))
            .andExpect(jsonPath("$[0].orderTableId").value(테이블9_사용중.getId()))
            .andExpect(jsonPath("$[0].orderStatus").value(테이블9주문.getOrderStatus().name()));
    }

    @Test
    @Order(3)
    @DisplayName("특정 주문의 상태를 변경한다")
    void changeOrderStatus() throws Exception {
        OrderRequest request = new OrderRequest(COMPLETION);

        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/api/orders/{id}/order-status", 테이블10주문.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(테이블10주문.getId()))
            .andExpect(jsonPath("$.orderTableId").value(테이블10_사용중.getId()))
            .andExpect(jsonPath("$.orderStatus").value(request.getOrderStatus().name()));
    }
}
