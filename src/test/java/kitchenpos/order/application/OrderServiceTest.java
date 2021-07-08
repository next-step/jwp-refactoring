package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import kitchenpos.ordertable.domain.NumberOfGuests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.error.InvalidRequestException;
import kitchenpos.common.error.NotFoundMenuException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.order.repository.OrderLineItemDao;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableDao;
import kitchenpos.utils.MenuCreator;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.order.domain.Order;

@DisplayName("주문 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderRequest orderRequest;

    @BeforeEach
    void setup() {
        order = Order.of(1L, COOKING);

        Long orderTableId = 1L;
        Long menuId = 1L;
        Long quantity = 1L;
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuId, quantity);

        orderRequest = new OrderRequest(orderTableId, Arrays.asList(orderLineItemRequest));
    }

    @DisplayName("사용자는 주문을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        Menu menu = MenuCreator.of("국밥", "순대", 1000, 1L, "순대국", 1000);
        // when
        when(orderDao.save(any())).thenReturn(order);
        when(menuDao.findAllById(any())).thenReturn(Arrays.asList(menu));
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable(1L, new NumberOfGuests(1), true)));
        OrderResponse createdOrder = orderService.create(orderRequest);

        // then
        assertThat(createdOrder.getOrderStatus()).isEqualTo(COOKING.name());
    }

    @DisplayName("사용자는 주문 리스트를 조회 할 수 있다.")
    @Test
    void findAll() {
        // given

        // when
        when(orderDao.findAll()).thenReturn(Arrays.asList(order));
        List<OrderResponse> orderResponses = orderService.list();
        // then
        assertThat(orderResponses.size()).isEqualTo(1);
        assertThat(orderResponses.get(0).getOrderStatus()).isEqualTo(COOKING.name());
    }

    @DisplayName("주문 생성 시 주문 상태를 요리중으로 한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderStatusRequest orderRequest = new OrderStatusRequest(MEAL.name());

        // when
        when(orderDao.findById(1L)).thenReturn(Optional.of(order));
        OrderResponse changedOrder = orderService.changeOrderStatus(1L, orderRequest);
        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @DisplayName("사용자는 주문시 주문테이블id, 그리고 메뉴id와 수량을 요청으로 한다.")
    @Test
    void createFailedByOrderLineItems() {
        // given
        // when
        orderRequest = new OrderRequest();
        // then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(InvalidRequestException.class);
    }

    @DisplayName("사용자는 주문시 주문테이블id, 그리고 메뉴id와 수량을 요청으로 한다.")
    @Test
    void createFailedByMenus() {
        // given
        List<OrderLineItemRequest> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItemRequest());
        orderLineItems.add(new OrderLineItemRequest());
        // when
        orderRequest = new OrderRequest(1L, orderLineItems);
        // then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotFoundMenuException.class);
    }

    @DisplayName("요청시 기입한 메뉴가 존재해야한다.")
    @Test
    void createFailedByOrderTable() {
        // given
        // when
        // then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotFoundMenuException.class);
    }
}