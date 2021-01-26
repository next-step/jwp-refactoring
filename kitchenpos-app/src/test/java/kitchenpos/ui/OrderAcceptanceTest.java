package kitchenpos.ui;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.ChangeEmptyTableRequest;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.NumberOfGuestsRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static kitchenpos.common.Fixtures.*;
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

        주문_등록_실패_미존재_메뉴(주문_테이블_ID);

        Long 주문_ID = 주문_등록(주문_테이블_ID);

        주문_목록_조회됨();

        주문_상태_업데이트(주문_ID, OrderStatus.MEAL);

        주문_상태_업데이트(주문_ID, OrderStatus.COMPLETION);

        종료된_주문_상태_업데이트_시도_실패(주문_ID);
    }

    @DisplayName("존재하지 않는 주문 테이블을 업데이트한다")
    @Test
    void testChangeEmptyNonExistentOrderTable() throws Exception {
        // given
        ChangeEmptyTableRequest orderTable = toNotEmptyTableRequest();

        // when & then
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 0L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("존재하지 않는 주문 테이블의 손님 수를 업데이트한다")
    @Test
    void testChangeNumberOfGuests_nonExistentOrderTable() throws Exception {
        // given
        NumberOfGuestsRequest numberOfGuestsRequest = numberOfGuestsRequest();

        // when & then
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 0L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(numberOfGuestsRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("존재하지 않는 주문 테이블 ID로 주문을 등록한다")
    @Test
    void testCreateOrder_withNonExistentTable() throws Exception {
        // given
        OrderRequest orderRequest = orderRequest()
                .orderTableId(0L)
                .build();

        // when & then
        mockMvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("빈 주문 테이블로 주문 등록한다")
    @Test
    void testCreateOrderWithEmptyOrderTable() throws Exception {
        // given
        OrderRequest orderRequest = orderRequest()
                .orderTableId(0L)
                .build();

        // when & then
        mockMvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("존재하지 않는 주문의 상태를 업데이트한다")
    @Test
    void testChangeOrderStatusWithNonExistent() throws Exception {
        // given
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(OrderStatus.COMPLETION);

        // when & then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 0L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    void 주문_등록_실패_미존재_메뉴(Long orderTableId) throws Exception {
        // given
        OrderRequest orderRequest = orderRequest()
                .orderTableId(orderTableId)
                .orderLineItems(Collections.singletonList(orderLineItemRequest().menuId(0L).build()))
                .build();

        // when & then
        mockMvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    Long 빈_주문_테이블_등록() throws Exception {
        // given
        OrderTableRequest orderTableRequest = emptyOrderTableRequest().build();

        // when & then
        MvcResult mvcResult = mockMvc.perform(post("/api/tables")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTableRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.numberOfGuests").value(orderTableRequest.getNumberOfGuests()))
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
        ChangeEmptyTableRequest orderTable = new ChangeEmptyTableRequest(false);

        // when & then
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTableId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderTableId))
                .andExpect(jsonPath("$.empty").value(Boolean.FALSE));
    }

    void 주문_테이블_손님_방문(Long orderTableId) throws Exception {
        // given
        NumberOfGuestsRequest numberOfGuestsRequest = numberOfGuestsRequest();

        // when & then
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(numberOfGuestsRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderTableId))
                .andExpect(jsonPath("$.numberOfGuests").value(4));
    }

    Long 주문_등록(Long orderTableId) throws Exception {
        // given
        OrderRequest orderRequest = orderRequest()
                .orderTableId(orderTableId)
                .build();

        // when & then
        MvcResult mvcResult = mockMvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
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

    void 주문_상태_업데이트(Long orderId, OrderStatus orderStatus) throws Exception {
        // given
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(orderStatus);

        // when & then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", orderId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.orderTableId").isNotEmpty())
                .andExpect(jsonPath("$.orderStatus").value(orderStatus.name()))
                .andExpect(jsonPath("$.orderedTime").isNotEmpty())
                .andExpect(jsonPath("$.orderLineItems", hasSize(1)));
    }

    void 종료된_주문_상태_업데이트_시도_실패(Long orderId) throws Exception {
        // given
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(OrderStatus.MEAL);

        // when & then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", orderId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
