package kitchenpos.ui;

import static kitchenpos.application.TableServiceTest.두명;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.RestControllerTest;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("주문 API")
@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest extends RestControllerTest<Order> {

    private static final String BASE_URL = "/api/orders";

    @MockBean
    private OrderService orderService;

    private Menu 치즈버거세트;
    private OrderTable 주문테이블;
    private OrderLineItem 첫번째_주문항목;
    private List<OrderLineItem> 주문_항목_목록;
    private Order 주문;
    private List<Order> 주문_목록;

    @BeforeEach
    void setup() {
        치즈버거세트 = new Menu();
        치즈버거세트.setId(1L);
        주문테이블 = new OrderTable(1L, 두명);
        첫번째_주문항목 = new OrderLineItem(1L, 1L, 1L, 1);
        주문_항목_목록 = new ArrayList<>(Arrays.asList(첫번째_주문항목));
        주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), 주문_항목_목록);
        주문_목록 = new ArrayList<>(Arrays.asList(주문));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(orderService.create(any())).willReturn(주문);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(주문);
        post(BASE_URL, 주문)
            .andExpect(content().string(responseBody));
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // Given
        given(orderService.list()).willReturn(주문_목록);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(주문_목록);
        get(BASE_URL)
            .andExpect(content().string(responseBody));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        // Given
        given(orderService.changeOrderStatus(any(), any())).willReturn(주문);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(주문);
        put(BASE_URL + String.format("/%d/order-status", 주문.getId()), 주문)
            .andExpect(content().string(responseBody));
    }

}