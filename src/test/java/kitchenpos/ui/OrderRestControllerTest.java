package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
@DisplayName("OrderRestController 클래스")
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Nested
    @DisplayName("POST /api/orders 은")
    class Describe_create {

        @Nested
        @DisplayName("등록할 주문이 주어지면")
        class Context_with_order {
            Order givenOrder = new Order();

            @BeforeEach
            void setUp() {
                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setMenuId(1L);
                orderLineItem.setQuantity(1);

                givenOrder.setOrderTableId(1L);
                givenOrder.setOrderLineItems(Collections.singletonList(orderLineItem));

                when(orderService.create(any(Order.class)))
                        .thenReturn(givenOrder);
            }

            @DisplayName("201 Created 와 생성된 주문을 응답한다.")
            @Test
            void It_responds_created_with_order() throws Exception {
                mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)

                        .content(objectMapper.writeValueAsString(givenOrder)))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenOrder)
                        ));
            }
        }
    }

    @Nested
    @DisplayName("GET /api/orders 는")
    class Describe_list {

        @Nested
        @DisplayName("등록된 주문 목록이 있으면")
        class Context_with_orders {
            List<Order> givenOrders;

            @BeforeEach
            void setUp() {
                Order order = new Order();
                givenOrders = Collections.singletonList(order);
                when(orderService.list())
                        .thenReturn(givenOrders);
            }

            @DisplayName("200 OK 와 메뉴 목록을 응답한다.")
            @Test
            void it_responds_ok_with_orders() throws Exception {
                mockMvc.perform(get("/api/orders"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenOrders)
                        ));
            }
        }
    }

    @Nested
    @DisplayName("Put /api/orders/{orderId}/order-status 는")
    class Describe_changeOrderStatus {

        @Nested
        @DisplayName("갱신할 주문 식별자와 주문 정보가 주어지면")
        class Context_with_order_id_and_order {
            Long givenOrderId = 1L;
            Order givenOrder = new Order();

            @BeforeEach
            void setUp() {

                givenOrder.setOrderStatus(OrderStatus.COOKING.name());

                when(orderService.changeOrderStatus(anyLong(), any(Order.class)))
                        .thenReturn(givenOrder);
            }

            @DisplayName("200 OK 상태와 갱신된 주문 정보를 응답한다.")
            @Test
            void It_responds_updated_order() throws Exception {
                mockMvc.perform(put("/api/orders/{orderId}/order-status", givenOrderId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(givenOrder)))
                        .andExpect(status().isOk())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenOrder)
                        ));
            }
        }
    }
}
