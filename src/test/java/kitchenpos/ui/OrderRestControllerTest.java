package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(OrderRestController.class)
@DisplayName("OrderRestController 클래스 테스트")
public class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderRestController orderRestController;

    @Nested
    @DisplayName("POST /api/orders")
    public class PostMethod {
        @Test
        @DisplayName("성공적으로 주문을 등록하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final Order order = setupSuccess();

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(order))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

            // then
            final Order orderResponse =
                    objectMapper.readValue(response.getContentAsString(), Order.class);
            assertThat(orderResponse.getId()).isPositive();
        }

        private Order setupSuccess() {
            final Order order = new Order();
            order.setId(1L);
            Mockito.when(orderRestController.create(Mockito.any())).thenReturn(ResponseEntity.ok(order));
            return order;
        }

        @Test
        @DisplayName("주문을 등록하는데 싪해하면 400 상태 코드를 응답받는다")
        public void errorBadRequest() throws Exception {
            // given
            final Order order = setupErrorBadRequest();

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(order))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private Order setupErrorBadRequest() {
            final Order order = new Order();
            Mockito.when(orderRestController.create(Mockito.any())).thenReturn(ResponseEntity.badRequest().build());
            return order;
        }
    }

    @Nested
    @DisplayName("GET /api/orders")
    public class ListMethod {
        @Test
        @DisplayName("성공적으로 주문 목록을 조회하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            setupSuccess();

            // when
            MockHttpServletResponse response = mockMvc.perform(get("/api/orders")
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        }

        private void setupSuccess() {
            final List<Order> orders = Stream.of(1, 2, 3)
                    .map(Long::new)
                    .map(id -> {
                        final Order order = new Order();
                        order.setId(id);
                        return order;
                    })
                    .collect(Collectors.toList());
            Mockito.when(orderRestController.list()).thenReturn(ResponseEntity.ok(orders));
        }
    }

    @Nested
    @DisplayName("PUT /api/orders/{orderId}/order-status")
    public class PutMethod {
        @Test
        @DisplayName("성공적으로 주문 상태 변경을 하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final Order order = setupSuccess();
            final Order newOrder = new Order();
            newOrder.setOrderStatus(OrderStatus.MEAL.name());

            // when
            MockHttpServletResponse response = mockMvc.perform(
                            put("/api/orders/" + order.getId() + "/order-status")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(newOrder))
                                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

            // then
            final Order orderResponse =
                    objectMapper.readValue(response.getContentAsString(), Order.class);
            assertThat(orderResponse.getOrderStatus()).isEqualTo(newOrder.getOrderStatus());
        }

        private Order setupSuccess() {
            final Order orderAfter = new Order();
            orderAfter.setOrderStatus(OrderStatus.MEAL.name());
            Mockito.when(orderRestController.changeOrderStatus(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(ResponseEntity.ok(orderAfter));
            final Order orderBefore = new Order();
            orderBefore.setId(1L);
            return orderBefore;
        }

        @Test
        @DisplayName("주문 상태 변경하는데 실패하면 400 상태 코드를 응답받는다")
        public void errorBadRequest() throws Exception {
            // given
            final Order order = setupErrorBadRequest();
            final Order newOrder = new Order();
            newOrder.setOrderStatus(OrderStatus.MEAL.name());

            // when
            MockHttpServletResponse response = mockMvc.perform(
                            put("/api/orders/" + order.getId() + "/order-status")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(newOrder))
                                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private Order setupErrorBadRequest() {
            final Order orderBefore = new Order();
            orderBefore.setId(1L);
            Mockito.when(orderRestController.changeOrderStatus(Mockito.anyLong(), Mockito.any()))
                    .thenReturn(ResponseEntity.badRequest().build());
            return orderBefore;
        }
    }
}
