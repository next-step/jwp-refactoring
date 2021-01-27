package kitchenpos.order;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.common.BaseContollerTest;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends BaseContollerTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("새로운 주문을 등록합니다.")
    void create() throws Exception {
        Order order = this.createOrder(OrderStatus.COOKING);

        주문_등록_요청(order, status().isCreated());
    }

    @Test
    @DisplayName("새로운 주문 등록 요청 시 주문항목이 비어 있는 경우 오류 발생")
    void createNoOrderLineOccurredException() {
        Order order = this.createOrder(OrderStatus.COOKING);
        order.setOrderLineItems(null);

        assertThatThrownBy(() -> {
            주문_등록_요청(order, status().is5xxServerError());
        }).isInstanceOf(NestedServletException.class).hasMessageContaining("IllegalArgumentException");
    }

    @Test
    @DisplayName("새로운 주문 등록 요청 시 찾을 수 없는 테이블이 있는 경우 오류 발생")
    void createWrongOrderTableOccurredException() {
        Order order = this.createOrder(OrderStatus.COOKING);
        order.setOrderTableId(100L);

        assertThatThrownBy(() -> {
            주문_등록_요청(order, status().is5xxServerError());
        }).isInstanceOf(NestedServletException.class).hasMessageContaining("IllegalArgumentException");
    }

    @Test
    @DisplayName("모든 주문을 조회합니다.")
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

    private void 주문_등록_요청(Order order, ResultMatcher xxServerError) throws Exception {
        this.mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(order)
                ))
                .andDo(print())
                .andExpect(jsonPath(".id").exists())
                .andExpect(xxServerError)
        ;
    }
}
