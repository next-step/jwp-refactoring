package kitchenpos.order.ui;

import kitchenpos.ControllerTest;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.OrderRestController;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("OrderRestController ui 테스트")
@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest extends ControllerTest {
    @MockBean
    private OrderService orderService;

    private OrderTable 테이블;
    private Order 주문;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        테이블 = new OrderTable(1L, 4, true);
        주문 = new Order(1L, 1L);
    }

    @DisplayName("주문 생성 api 테스트")
    @Test
    void createOrder() throws Exception {
        given(orderService.create(any(Order.class))).willReturn(주문);

        mockMvc.perform(post("/api/orders")
                        .content(mapper.writeValueAsBytes(주문))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @DisplayName("주문 조회 api 테스트")
    @Test
    void listOrder() throws Exception {
        given(orderService.list()).willReturn(Lists.newArrayList(주문));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk());
    }

    @DisplayName("주문 상태 변경 api 테스트")
    @Test
    void changeOrderStatus() throws Exception {
        주문.setOrderStatus("MEAL");
        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(주문);

        mockMvc.perform(put("/api/orders/{orderId}/order-status", 주문.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(주문)))
                .andExpect(status().isOk());
    }
}
