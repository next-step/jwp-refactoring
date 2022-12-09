package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private Order order = new Order();
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @Test
    void 주문을_등록할_수_있다() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        given(order.getOrderLineItems()).willReturn(Collections.singletonList(new OrderLineItem()));
        given(menuDao.countByIdIn(any())).willReturn(1l);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);

        Order createOrder = orderService.create(order);

        assertThat(createOrder).isEqualTo(order);
    }

    @Test
    void 등록_된_메뉴만_지정할_수_있다() {
        Order 없는_메뉴가_포함된_주문 = new Order();

        ThrowingCallable 없는_메뉴가_포함된_주문시도 = () -> orderService.create(없는_메뉴가_포함된_주문);

        assertThatIllegalArgumentException().isThrownBy(없는_메뉴가_포함된_주문시도);
    }

    @Test
    void 등록_된_주문_테이블만_지정할_수_있다() {
        given(order.getOrderLineItems()).willReturn(Collections.singletonList(new OrderLineItem()));
        given(menuDao.countByIdIn(any())).willReturn(1l);
        given(orderTableDao.findById(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록_되지_않은_주문_테이블_지정 = () -> orderService.create(order);

        assertThatIllegalArgumentException().isThrownBy(등록_되지_않은_주문_테이블_지정);
    }

    @Test
    void 주문_테이블은_비어있으면_안된다() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, true);
        given(order.getOrderLineItems()).willReturn(Collections.singletonList(new OrderLineItem()));
        given(menuDao.countByIdIn(any())).willReturn(1l);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        ThrowingCallable 빈_주문_테이블일_경우 = () -> orderService.create(order);

        assertThatIllegalArgumentException().isThrownBy(빈_주문_테이블일_경우);
    }

    @Test
    void 주문_목록을_조회할_수_있다() {
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));

        List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders).containsExactly(order)
        );
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        given(orderDao.findById(any())).willReturn(Optional.of(order));
        given(order.getOrderStatus()).willReturn(OrderStatus.COOKING.name());

        Order saveOrder = orderService.changeOrderStatus(1L, this.order);

        assertThat(saveOrder).isEqualTo(order);
    }

    @Test
    void 등록_된_주문의_상태만_변경할_수_있다() {
        given(orderDao.findById(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록되지_않은_주문의_상태변경 = () -> orderService.changeOrderStatus(1L, order);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문의_상태변경);
    }

    @Test
    void 이미_완료된_주문의_상태는_변경할_수_없다() {
        given(orderDao.findById(any())).willReturn(Optional.of(order));
        given(order.getOrderStatus()).willReturn(OrderStatus.COMPLETION.name());

        ThrowingCallable 이미_완료된_주문의_상태_변경 = () -> orderService.changeOrderStatus(1L, order);

        assertThatIllegalArgumentException().isThrownBy(이미_완료된_주문의_상태_변경);
    }
}
