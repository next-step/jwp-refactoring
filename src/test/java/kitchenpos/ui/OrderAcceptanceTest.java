package kitchenpos.ui;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends BaseControllerTest {

    @DisplayName("주문 테이블 관리")
    @Test
    void testManageTable() throws Exception {
        Long 주문_테이블_ID = 빈_주문_테이블_등록();

        주문_테이블_추가됨_목록_조회();

        주문_테이블_주문_등록_가능(주문_테이블_ID);

        주문_테이블_손님_방문(주문_테이블_ID);

        Long 주문_ID = 주문_등록(주문_테이블_ID);

        주문_목록_조회됨();

        주문_상태_식사로_업데이트(주문_ID);
    }

    Long 빈_주문_테이블_등록() throws Exception {
        // given
        int numberOfGuests = 0;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(true);

        // when & then
        MvcResult mvcResult = mockMvc.perform(post("/api/tables")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.numberOfGuests").value(numberOfGuests))
                .andExpect(jsonPath("$.empty").value(Boolean.TRUE))
                .andReturn();

        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderTable.class)
                .getId();
    }

    void 주문_테이블_추가됨_목록_조회() throws Exception {
        mockMvc.perform(get("/api/tables")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(9)));
    }

    void 주문_테이블_주문_등록_가능(Long orderTableId) throws Exception {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when & then
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTableId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empty").value(Boolean.FALSE));
    }

    void 주문_테이블_손님_방문(Long orderTableId) throws Exception {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);

        // when & then
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests").value(4));
    }

    Long 주문_등록(Long orderTableId) throws Exception {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when & then
        MvcResult mvcResult = mockMvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderTableId").value(orderTableId))
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.COOKING.name()))
                .andExpect(jsonPath("$.orderedTime").isNotEmpty())
                .andExpect(jsonPath("$.orderLineItems", hasSize(1)))
                .andReturn();

        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Order.class)
                .getId();
    }

    void 주문_목록_조회됨() throws Exception {
        mockMvc.perform(get("/api/orders")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    void 주문_상태_식사로_업데이트(Long orderId) throws Exception {
        // given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when & then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", orderId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.MEAL.name()));
    }
}
