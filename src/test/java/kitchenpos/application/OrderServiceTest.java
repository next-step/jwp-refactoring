package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
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
    private MenuRepository menuRepository;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 1L, 3);
        orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
    }

    @Test
    @DisplayName("주문은 주문항목이 없으면 주문을 할 수 없다")
    void orderItemEmptyCreateOrder() {
        //given
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.create(order)
        );
    }

    @Test
    @DisplayName("등록되지 않은 메뉴를 주문하면 주문을 할 수 없다.")
    void registerNotMenuOrder() {
        //given
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        given(menuRepository.countByIdIn(anyList())).willReturn(0L);


        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.create(order)
        );
    }

    @Test
    @DisplayName("존재하지 않은 테이블에 주문 할 수 없다.")
    void existNotOrderTable() {
        //gvien
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.create(order)
        );
    }

    @Test
    @DisplayName("주문 받을 수 있는 주문 테이블이 없다면 주문을 할 수 없다.")
    void emptyOrderTable() {
        //gvien
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

        given(menuRepository.countByIdIn(anyList())).willReturn(2L);
        OrderTable emptyTable = new OrderTable(1L, 3, true);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(emptyTable));

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.create(order)
        );
    }

    @Test
    @DisplayName("주문을 한다.")
    void createOrder() {
        //gvien
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);

        OrderTable orderTable = new OrderTable(1L, 3, false);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(orderLineItems.get(0))).willReturn(orderLineItems.get(0));
        given(orderLineItemDao.save(orderLineItems.get(1))).willReturn(orderLineItems.get(1));

        //when
        Order saveOrder = orderService.create(order);

        //then
        assertAll(
                () -> assertThat(saveOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(saveOrder.getOrderLineItems()).hasSize(2)
        );
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void orderList() {
        //gvien
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(orderLineItems);

        //when
        List<Order> orders = orderService.list();

        //then
        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0).getId()).isEqualTo(order.getId())
        );
    }

    @Test
    @DisplayName("주문이 존재하지 않으면 변경할 수 없다.")
    void orderIsNotExistNotChange() {
        //given
        given(orderDao.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.changeOrderStatus(1L, new Order())
        );
    }

    @Test
    @DisplayName("주문이 완료가 되었으면 경우에는 주문을 변경 할 수 없다.")
    void orderStatusCompleteNotChange() {
        //given
        Order order = new Order(1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems);
        given(orderDao.findById(1L)).willReturn(Optional.of(order));

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.changeOrderStatus(1L, new Order())
        );
    }

    @Test
    @DisplayName("주문이 상태가 변경이 된다.")
    void orderStatusChange() {
        //given
        Order beforeOrder = new Order(1L,1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        List<OrderLineItem> beforeOrderItems = orderLineItems;
        given(orderDao.findById(beforeOrder.getId())).willReturn(Optional.of(beforeOrder));
        given(orderLineItemDao.findAllByOrderId(beforeOrder.getId())).willReturn(beforeOrderItems);

        //when
        final Order changeStatusOrder = orderService.changeOrderStatus(1L,
                new Order(1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems));
        //then
        assertThat(changeStatusOrder.getOrderStatus()).isEqualTo( OrderStatus.COMPLETION.name());
    }

}