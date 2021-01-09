package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
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
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        Order order = new Order();
        order.setOrderTableId(7L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        Long orderTableId = order.getOrderTableId();

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        mockMvc.perform(put("/api/tables/"+ orderTableId +"/empty")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(orderTable)));

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
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        Order order = new Order();
        order.setOrderTableId(8L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        Long orderTableId = order.getOrderTableId();

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        mockMvc.perform(put("/api/tables/"+ orderTableId +"/empty")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(orderTable)));

        String url = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        order.setOrderStatus("COMPLETION");
        mockMvc.perform(put(url + "/order-status")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus").value("COMPLETION"));

    }


}
