package kitchenpos.order;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 관련 기능")
class OrderTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문이 존재하지 않으면 예외가 발생한다.")
    void createOrderFailBecauseOfIsEmptyOrderLineItem() {
        // given
        final Order order = new Order(Collections.emptyList());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.create(order);
        });
    }

    @Test
    @DisplayName("주문한 메뉴가 존재하지 않으면 예외가 발생한다.")
    void createOrderFailBecauseOfIsNotExistMenu() {
        // given
        final Order order = new Order(Arrays.asList(new OrderLineItem()));
        when(menuDao.countByIdIn(anyList())).thenReturn(2L);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.create(order);
        });
    }

    @Test
    @DisplayName("테이블이 존재하지 않으면 예외가 발생한다.")
    void createOrderFailBecauseOfIsNotExistTable() {
        // given
        final Order order = new Order(Arrays.asList(new OrderLineItem()));
        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.create(order);
        });
    }

    @Test
    @DisplayName("테이블의 상태가 사용불가라면 예외가 발생한다.")
    void createOrderFailBecauseOfIsEmptyTableStatus() {
        // given
        final Order order = new Order(Arrays.asList(new OrderLineItem()));
        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable(true)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.create(order);
        });
    }

    @Test
    @DisplayName("주문을 할 수 있다.")
    void createOrder() {
        // given
        final Order order = new Order(Arrays.asList(new OrderLineItem()));
        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable(1L, false)));
        when(orderDao.save(any())).thenReturn(new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now()));
        when(orderLineItemDao.save(any())).thenReturn(new OrderLineItem(1L,1L,1L,1L));

        // when
        final Order savedOrder = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(savedOrder.getId()).isOne(),
                () -> assertThat(savedOrder.getOrderTableId()).isOne(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderLineItems().size()).isOne()
        );
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다.")
    void findOrder() {
        // given
        when(orderDao.findAll()).thenReturn(Arrays.asList(new Order()));
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(new OrderLineItem(), new OrderLineItem()));

        // when
        final List<Order> findByOrder = orderService.list();

        // then
        assertAll(
                () -> assertThat(findByOrder.size()).isOne(),
                () -> assertThat(findByOrder.get(0).getOrderLineItems().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("주문 상태 변경 시 주문이 존재하지 않으면 예외가 발생한다.")
    void changeOrderStatusFailBecauseOfIsNotExistOrder() {
        // given
        when(orderDao.findById(any())).thenReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.changeOrderStatus(1L, new Order());
        });
    }

    @Test
    @DisplayName("주문 상태 변경 시 주문이 완료 상태면 예외가 발생한다.")
    void changeOrderStatusFailBecauseOfOrderStatusCompletion() {
        // given
        when(orderDao.findById(any())).thenReturn(Optional.of(new Order(OrderStatus.COMPLETION.name())));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.changeOrderStatus(1L, new Order());
        });
    }

    @Test
    @DisplayName("주문의 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        when(orderDao.findById(any())).thenReturn(Optional.of(new Order(OrderStatus.MEAL.name())));

        // when
        final Order savedOrder = orderService.changeOrderStatus(1L, new Order(OrderStatus.COOKING.name()));

        // then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }
}
