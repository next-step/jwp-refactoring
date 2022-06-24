package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private OrderService orderService;
    private OrderLineItem orderLineItem;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        // given
        orderLineItem = new OrderLineItem(1L, 1L);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        // given
        final Order order = new Order(null, 1L, OrderStatus.COOKING.name(), null, null);
        when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable(1L, null, 3, false)));
        when(orderDao.save(any())).thenReturn(order);
        when(orderLineItemDao.save(orderLineItem)).thenReturn(orderLineItem);
        // when
        final Order actual = orderService.create(new OrderRequest(1L, Arrays.asList(1L)));
        // then
        assertAll(
                () -> assertThat(actual.getOrderLineItems()).hasSize(1),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderTableId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("주문 할 메뉴가 없으면 에러 발생")
    void invalidZeroOfOrderLineItem() {
        // given
        final OrderRequest invalidOrderRequest = new OrderRequest(1L, Collections.emptyList());

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(invalidOrderRequest));
    }

    @Test
    @DisplayName("등록되지 않은 메뉴 주문시 에러 발생")
    void orderNotExistMenu() {
        // given
        final OrderRequest notExistMenuOrderRequest = new OrderRequest(1L, Arrays.asList(1L));

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(notExistMenuOrderRequest));
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않으면 에러 발생")
    void notExistOrderTable() {
        // given
        final Order order = new Order(null, 1L, OrderStatus.COOKING.name(), null, null);
        when(menuDao.countByIdIn(Arrays.asList(1L))).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(new OrderRequest(1L, Arrays.asList(1L))));
    }

    @Test
    @DisplayName("주문 내역을 조회할 수 있다.")
    void searchOrders() {
        // given
        final Order order = new Order(1L, Arrays.asList(orderLineItem));
        when(orderDao.findAll()).thenReturn(Arrays.asList(order));
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem));
        // when
        final List<Order> actual = orderService.list();
        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getOrderLineItems()).hasSize(1),
                () -> assertThat(actual.get(0).getOrderLineItems()).contains(orderLineItem)
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        final Order order = new Order(1L, Arrays.asList(orderLineItem));
        when(orderDao.findById(any())).thenReturn(Optional.of(order));
        when(orderDao.save(any())).thenReturn(order);
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem));
        // when
        final Order actual = orderService.changeOrderStatus(1L, new Order(OrderStatus.COOKING.name()));
        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 내역이 존재하지 않은 주문 상태 변경시 에러 발생")
    void changeOrderStatusNotExistOrder() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(1L, new Order(OrderStatus.COOKING.name())));
    }

    @Test
    @DisplayName("완료된 주문을 주문 상태 변경시 에러 발생")
    void changeOrderStatusCompletionOrder() {
        // given
        final Order order = new Order(null, 1L, OrderStatus.COMPLETION.name(), null, Arrays.asList(orderLineItem));
        when(orderDao.findById(any())).thenReturn(Optional.of(order));

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(1L, new Order(OrderStatus.COOKING.name())));
    }
}
