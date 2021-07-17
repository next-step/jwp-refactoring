package kitchenpos.ui;

import static java.util.Collections.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.OrderService;
import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

class OrderRestControllerTest extends BaseControllerTest {

    @MockBean
    private OrderService orderService;

    private Order 주문;
    private OrderTable 테이블_1번;

    @BeforeEach
    void setUp() {
        테이블_1번 = new OrderTable();
        테이블_1번.setId(1L);
        테이블_1번.setEmpty(false);
        테이블_1번.setNumberOfGuests(3);

        Menu 면종류 = new Menu();
        면종류.setId(1L);
        면종류.setName("면종류");
        면종류.setPrice(new BigDecimal(10000L));

        OrderLineItem 주문항목1 = new OrderLineItem();
        주문항목1.setSeq(1L);
        주문항목1.setQuantity(5L);
        주문항목1.setMenuId(면종류.getId());

        주문 = new Order();
        주문.setId(1L);
        주문.setOrderTableId(테이블_1번.getId());
        주문.setOrderStatus(OrderStatus.COOKING.name());
        주문.setOrderLineItems(singletonList(주문항목1));
        주문항목1.setOrderId(주문.getId());
    }

    @DisplayName("주문 생성")
    @Test
    void create() throws Exception {
        String jsonString = objectMapper.writeValueAsString(주문);
        when(orderService.create(isA(Order.class))).thenReturn(주문);

        mockMvc.perform(
            post("/api/orders")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString)
        ).andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(주문.getId()));
    }

    @Test
    void list() throws Exception {
        when(orderService.list()).thenReturn(singletonList(주문));
        mockMvc.perform(
            get("/api/orders")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.[*]").isNotEmpty());
    }

    @Test
    void changeOrderStatus() throws Exception {
        when(orderService.changeOrderStatus(eq(주문.getId()), isA(Order.class))).thenReturn(주문);

        String jsonString = objectMapper.writeValueAsString(주문);
        mockMvc.perform(
            put("/api/orders/" + 주문.getId() + "/order-status")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(주문.getId()));
    }
}