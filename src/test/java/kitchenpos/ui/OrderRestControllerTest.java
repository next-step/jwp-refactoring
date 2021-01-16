package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemCreateRequest;
import kitchenpos.dto.OrderStatus;
import kitchenpos.dto.OrderStatusChangeDto;
import kitchenpos.dto.OrderTableEmptyChangeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-08
 */
class OrderRestControllerTest extends BaseControllerTest {

    @DisplayName("주문 생성")
    @Test
    public void menuGroupCreateTest() throws Exception {

        OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest order = new OrderCreateRequest(7L, Collections.singletonList(orderLineItem));
        Long orderTableId = order.getOrderTableId();

        OrderTableEmptyChangeRequest changeRequest = new OrderTableEmptyChangeRequest(false);
        mockMvc.perform(put("/api/tables/"+ orderTableId +"/empty")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(changeRequest)));

        mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("orderTableId").value(orderTableId))
                .andExpect(jsonPath("orderStatus").value("COOKING"))
                .andExpect(jsonPath("orderLineItems[0].menuId")
                        .value(orderLineItem.getMenuId()))
                .andExpect(jsonPath("orderLineItems[0].quantity")
                        .value(orderLineItem.getQuantity()));
    }


    @DisplayName("주문 조회")
    @Test
    public void menuGroupSelectTest() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("주문 상태 변경")
    @Test
    public void menuGroupChangeOrderStateTest() throws Exception {
        OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(8L, Collections.singletonList(orderLineItem));

        Long orderTableId = orderRequest.getOrderTableId();
        OrderTableEmptyChangeRequest statusChangeRequest = new OrderTableEmptyChangeRequest(false);
        mockMvc.perform(put("/api/tables/"+ orderTableId +"/empty")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(statusChangeRequest)));

        String url = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        OrderStatusChangeDto orderStatusChangeRequest = new OrderStatusChangeDto(OrderStatus.COMPLETION);
        mockMvc.perform(put(url + "/order-status")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(orderStatusChangeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus").value("COMPLETION"));

    }


}
