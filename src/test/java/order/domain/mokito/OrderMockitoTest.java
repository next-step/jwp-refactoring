package order.domain.mokito;

import static fixture.MenuFixture.*;
import static fixture.OrderFixture.*;
import static fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import menu.repository.*;
import order.application.OrderService;
import order.dto.*;
import order.repository.*;
import table.repository.*;

@DisplayName("주문 관련(Mockito)")
class OrderMockitoTest {
    private MenuRepository menuRepository;
    private OrderRepository orderRepository;
    private OrderTableRepository orderTableRepository;
    private OrderService orderService;

    @BeforeEach
    void setup() {
        this.menuRepository = mock(MenuRepository.class);
        this.orderRepository = mock(OrderRepository.class);
        this.orderTableRepository = mock(OrderTableRepository.class);
        this.orderService = OrderService.of(menuRepository, orderRepository, orderTableRepository);
    }

    @DisplayName("주문 생성하기")
    @Test
    void createTest() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(주문테이블_4명));
        when(orderRepository.save(any())).thenReturn(주문_첫번째);
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(메뉴_후라이드치킨));

        OrderLineItemRequest orderLineItemRequest = OrderLineItemRequest.of(anyLong(), 1);
        OrderRequest orderRequest = OrderRequest.of(1L, Lists.newArrayList(orderLineItemRequest));

        assertThat(orderService.saveOrder(orderRequest)).isInstanceOf(OrderResponse.class);
    }

    @DisplayName("주문 조회하기")
    @Test
    void findAllTest() {
        when(orderRepository.findAll()).thenReturn(Lists.newArrayList(주문_첫번째, 주문_두번째));
        assertThat(orderService.findAll()).containsExactly(OrderResponse.from(주문_첫번째), OrderResponse.from(주문_두번째));
    }
}
