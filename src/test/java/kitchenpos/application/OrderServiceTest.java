package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MenuDao menuDao;
    @Mock
    OrderDao orderDao;
    @Mock
    OrderLineItemDao orderLineItemDao;
    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    OrderService orderService;

    private Menu menu;
    private OrderTable orderTable;
    private OrderLineItem orderLineItem;
    private Order order;

    @BeforeEach
    void setUp() {
        menu = new Menu(1L, "후라이드한마리", BigDecimal.valueOf(10000), null, null);
        orderTable = new OrderTable(1L, null, 10, false);
        orderLineItem = new OrderLineItem(null, null, 1L, 2);
        order = new Order(1L, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItem));
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));
        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.save(any())).willReturn(orderLineItem);
        //when
        orderService.create(order);
        //then
        assertAll(
                () -> assertThat(order.getId()).isNotNull(),
                () -> assertThat(order.getOrderedTime()).isNotNull(),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(order.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(order.getOrderLineItems()).contains(orderLineItem)
        );
    }

    @DisplayName("주문을 생성 요청시 주문 항목이 없으면 에러가 발생한다.")
    @Test
    void createOrderLineItems() {
        //given
        Order order = new Order(1L, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.emptyList());

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목에 등록되지 않은 메뉴가 있으면 에러가 발생한다.")
    @Test
    void createOrderItemCount() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(0L);

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 에러가 발생한다.")
    @Test
    void createOrderTable() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어 있으면 에러가 발생한다.")
    @Test
    void createOrderTableEmpty() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(new OrderTable(1L, null, 0, true)));

        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(Collections.singletonList(orderLineItem));
        //when
        List<Order> list = orderService.list();
        //then
        assertThat(list).contains(order);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        //given
        Order changeStatus = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        given(orderDao.findById(1L)).willReturn(Optional.ofNullable(order));
        //when
        Order expect = orderService.changeOrderStatus(1L, changeStatus);
        //then
        assertThat(expect.getOrderStatus()).isEqualTo(changeStatus.getOrderStatus());
    }

    @DisplayName("존재하는 주문만 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusExist() {
        //given
        Order changeStatus = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        given(orderDao.findById(1L)).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeStatus))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 상태가 완료이면 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusCompletion() {
        //given
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        Order changeStatus = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        given(orderDao.findById(1L)).willReturn(Optional.of(order));
        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeStatus))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
