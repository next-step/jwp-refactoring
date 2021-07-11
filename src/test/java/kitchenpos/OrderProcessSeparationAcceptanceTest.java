package kitchenpos;

import static kitchenpos.domain.MenuProductTest.*;
import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.domain.OrderTableTest.*;
import static kitchenpos.domain.ProductTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.domain.Price;
import kitchenpos.dto.OrderLineItemDetailRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("주문 프로세스 분리 인수 테스트")
public class OrderProcessSeparationAcceptanceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    OrderLineItemDetailRequest 후라이드_주문목록_상세_요청;
    OrderLineItemRequest 후라이드_주문목록_요청;
    OrderRequest 후라이드_주문_요청;

    @BeforeEach
    void setUp() {
        후라이드_주문목록_상세_요청 = new OrderLineItemDetailRequest(MP1후라이드.getSeq(), 후라이드.getId(), "후라이드", BigDecimal.valueOf(16000), 1);
        후라이드_주문목록_요청 = new OrderLineItemRequest(1L, 후라이드_메뉴.getName(), Price.valueOf(200).value(), 1, Arrays.asList(후라이드_주문목록_상세_요청));
        후라이드_주문_요청 = new OrderRequest(테이블12_사용중_주문전.getId(), Arrays.asList(후라이드_주문목록_요청));
    }

    @Test
    @DisplayName("주문 요청정보가 주문스펙과 일치할 경우 성공")
    void request_succeed() throws Exception {
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(후라이드_주문_요청)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.orderTableId").value(테이블12_사용중_주문전.getId()))
            .andExpect(jsonPath("$.orderStatus").value(COOKING.name()));
    }

    @Test
    @DisplayName("주문한 메뉴명이 실제 메뉴명과 다를 경우 실패")
    void request_failed_menu_name() throws Exception {
        후라이드_주문목록_요청.setName("옛날통닭이었던후라이드");
        
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(후라이드_주문_요청)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문한 메뉴가격이 실제 메뉴가격과 다를 경우 실패")
    void request_failed_menu_price() throws Exception {
        후라이드_주문목록_요청.setPrice(BigDecimal.ONE);
        
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(후라이드_주문_요청)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문한 메뉴의 제품 수량이 실제 제품 수량과 다를 경우 실패")
    void request_failed_menu_product_quantity() throws Exception {
        후라이드_주문목록_상세_요청.setQuantity(100);
        
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(후라이드_주문_요청)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문한 메뉴의 제품명이 실제 제품명과 다를 경우 실패")
    void request_failed_menu_product_name() throws Exception {
        후라이드_주문목록_상세_요청.setName("6000원이었던후라이드");
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(후라이드_주문_요청)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문한 메뉴의 제품가격이 실제 제품가격과 다를 경우 실패")
    void request_failed_menu_product_price() throws Exception {
        후라이드_주문목록_상세_요청.setPrice(BigDecimal.ONE);
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(후라이드_주문_요청)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
}
