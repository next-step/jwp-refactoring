package kitchenpos.order.ui;

import static java.util.Collections.*;
import static kitchenpos.menu.domain.MenuProductTest.*;
import static kitchenpos.menu.domain.MenuTest.*;
import static kitchenpos.order.domain.OrderStatus.*;
import static kitchenpos.order.domain.OrderTableTest.*;
import static kitchenpos.order.domain.OrderTest.*;
import static kitchenpos.product.domain.ProductTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.order.dto.OrderLineItemDetailRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.utils.IntegrationTest;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("주문 통합 테스트")
class OrderRestControllerTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    OrderLineItemDetailRequest 후라이드_주문_내역_상세;
    OrderLineItemRequest 후라이드_주문_내역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드_주문_내역_상세 = new OrderLineItemDetailRequest(MP1후라이드.getSeq(), 후라이드.getId(), "후라이드", BigDecimal.valueOf(16000), 1);
        후라이드_주문_내역 = new OrderLineItemRequest(1L, 후라이드_메뉴.getName(), 후라이드_메뉴.getPrice().value(), 1, singletonList(후라이드_주문_내역_상세));
    }

    @Test
    @DisplayName("주문을 생성한다")
    void create() throws Exception {
        OrderRequest request = new OrderRequest(테이블12_사용중_주문전.getId(), singletonList(후라이드_주문_내역));
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.orderStatus").value(COOKING.name()));
    }

    @Test
    @DisplayName("생성이 실패한다 - 주문 항목이 비어있을 경우")
    void create_failed_1() throws Exception {
        OrderRequest request = new OrderRequest(테이블12_사용중_주문전.getId(), Collections.emptyList());
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("생성이 실패한다 - 메뉴가 존재하지 않을 경우")
    void create_failed_2() throws Exception {
        Long invalidId = -1L;
        후라이드_주문_내역.setMenuId(invalidId);
        OrderRequest request = new OrderRequest(테이블12_사용중_주문전.getId(), singletonList(후라이드_주문_내역));
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("생성이 실패한다 - 테이블이 존재하지 않을 경우")
    void create_failed_3() throws Exception {
        Long invalidId = -1L;
        OrderRequest request = new OrderRequest(invalidId, singletonList(후라이드_주문_내역));
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("생성이 실패한다 - 테이블이 비어있을 경우")
    void create_failed_4() throws Exception {
        OrderRequest request = new OrderRequest(테이블8_빈자리.getId(), singletonList(후라이드_주문_내역));
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("주문 목록을 가져온다")
    void list() throws Exception {
        mockMvc.perform(get("/api/orders"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(테이블9주문_조리.getId()))
            .andExpect(jsonPath("$[0].orderStatus").value("COOKING"));
    }

    @Test
    @DisplayName("특정 주문의 상태를 변경한다")
    void changeOrderStatus() throws Exception {
        OrderRequest request = new OrderRequest(COMPLETION);
        mockMvc.perform(put("/api/orders/{id}/order-status", 테이블10주문_식사.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(테이블10주문_식사.getId()))
            .andExpect(jsonPath("$.orderStatus").value(COMPLETION.name()));
    }

    @Test
    @DisplayName("상태 변경이 실패한다 - 주문이 존재하지 않을 경우")
    void changeOrderStatus_failed_1() throws Exception {
        Long invalidId = -1L;
        OrderRequest request = new OrderRequest(COMPLETION);
        mockMvc.perform(put("/api/orders/{id}/order-status", invalidId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상태 변경이 실패한다 - 이미 완결 된 주문일 경우")
    void changeOrderStatus_failed_2() throws Exception {
        OrderRequest request = new OrderRequest(COMPLETION);
        mockMvc.perform(put("/api/orders/{id}/order-status", 테이블11주문_완결.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isConflict());
    }
}
