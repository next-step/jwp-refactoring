package kitchenpos.order;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.common.BaseContollerTest;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends BaseContollerTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("새로운 메뉴를 등록합니다.")
    void create() throws Exception {
        Order order = this.createOrder(OrderStatus.COOKING);

        this.mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(order)
                ))
                .andDo(print())
                .andExpect(jsonPath(".id").exists())
                .andExpect(status().isCreated())
        ;
    }


    @Test
    @DisplayName("모든 메뉴 목록을 조회합니다.")
    void findAll() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").exists())
                .andReturn();

        String responseOrders = mvcResult.getResponse().getContentAsString();
        ArrayList<Order> orders
                = this.objectMapper.readValue(responseOrders, new TypeReference<ArrayList<Order>>() {});

        assertThat(orders).hasSize(1);
    }


    private Order createOrder(OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(this.getOrderTable().getId());
        order.setOrderLineItems(this.getOrderLineItems());

        return order;
    }

    private OrderTable getOrderTable() {
        OrderTable orderTable = this.orderTableDao.findAll().stream().findFirst().get();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);

        return this.orderTableDao.save(orderTable);
    }

    private List<OrderLineItem> getOrderLineItems() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(4);
        return Arrays.asList(new OrderLineItem[]{orderLineItem});
    }
}
