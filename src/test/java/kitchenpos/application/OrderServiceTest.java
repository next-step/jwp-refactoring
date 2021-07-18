package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static kitchenpos.fixture.OrderFixture.주문_후라이드_한마리_감자튀김_계산완료;
import static kitchenpos.fixture.OrderFixture.주문_후라이드_한마리_감자튀김_조리중;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_고객_3명의_빈_테이블;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_조리_중인_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.when;

@TestInstance(PER_CLASS)
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

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @Test
    void create_예외_빈_주문상품_리스트() {

        // given
        Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(order));
    }

    @Test
    void create_예외_저장되지_않은_메뉴() {
        // given
        final List<Long> menuIds = 주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        // when
        when(menuDao.countByIdIn(menuIds)).thenReturn(1L);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(주문_후라이드_한마리_감자튀김_조리중));
    }

    @Test
    void create_존재하지_않는_주문_테이블() {
        // given
        final List<Long> menuIds = 주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        // when
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) 주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems().size());
        when(orderTableDao.findById(주문_후라이드_한마리_감자튀김_조리중.getOrderTableId())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(주문_후라이드_한마리_감자튀김_조리중));
    }

    @Test
    void create_빈_주문_테이블() {
        // given
        final List<Long> menuIds = 주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        // when
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) 주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems().size());
        when(orderTableDao.findById(주문_후라이드_한마리_감자튀김_조리중.getOrderTableId())).thenReturn(Optional.of(주문_테이블_고객_3명의_빈_테이블));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(주문_후라이드_한마리_감자튀김_조리중));
    }

    @Test
    void create_성공() {
        // given
        final List<Long> menuIds = 주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        // when
        when(menuDao.countByIdIn(menuIds)).thenReturn((long) 주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems().size());
        when(orderTableDao.findById(주문_후라이드_한마리_감자튀김_조리중.getOrderTableId())).thenReturn(Optional.of(주문_테이블_조리_중인_주문_테이블));
        when(orderDao.save(주문_후라이드_한마리_감자튀김_조리중)).thenReturn(주문_후라이드_한마리_감자튀김_조리중);
        for (OrderLineItem orderLineItem : 주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems()) {
            when(orderLineItemDao.save(orderLineItem)).thenReturn(orderLineItem);
        }

        // then
        assertDoesNotThrow(() -> orderService.create(주문_후라이드_한마리_감자튀김_조리중));
    }

    @Test
    void list_성공() {
        // when
        when(orderDao.findAll()).thenReturn(asList(주문_후라이드_한마리_감자튀김_조리중));
        for (OrderLineItem orderLineItem : 주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems()) {
            when(orderLineItemDao.findAllByOrderId(orderLineItem.getOrderId())).thenReturn(asList(orderLineItem));
        }
        List<Order> order = orderService.list();

        // then
        assertThat(order).containsExactly(주문_후라이드_한마리_감자튀김_조리중);
    }

    @Test
    void changeOrderStatus_예외_존재하지_않는_주문() {
        // given
        Order givenOrder = new Order(
                주문_후라이드_한마리_감자튀김_조리중.getId(),
                주문_후라이드_한마리_감자튀김_조리중.getOrderTableId(),
                주문_후라이드_한마리_감자튀김_조리중.getOrderStatus(),
                주문_후라이드_한마리_감자튀김_조리중.getOrderedTime(),
                주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems()
        );
        Order argumentOrder = new Order(
                null,
                null,
                OrderStatus.MEAL.name(),
                null,
                null
        );

        // when
        when(orderDao.findById(givenOrder.getId())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(givenOrder.getId(), argumentOrder));
    }

    @Test
    void changeOrderStatus_예외_이미_계산_완료된_주문() {
        // given
        Order givenOrder = new Order(
                주문_후라이드_한마리_감자튀김_계산완료.getId(),
                주문_후라이드_한마리_감자튀김_계산완료.getOrderTableId(),
                주문_후라이드_한마리_감자튀김_계산완료.getOrderStatus(),
                주문_후라이드_한마리_감자튀김_계산완료.getOrderedTime(),
                주문_후라이드_한마리_감자튀김_계산완료.getOrderLineItems()
        );
        Order argumentOrder = new Order(
                null,
                null,
                OrderStatus.MEAL.name(),
                null,
                null
        );

        // when
        when(orderDao.findById(givenOrder.getId())).thenReturn(Optional.of(givenOrder));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(givenOrder.getId(), argumentOrder));
    }

    @Test
    void changeOrderStatus_성공() {
        // given
        Order givenOrder = new Order(
                주문_후라이드_한마리_감자튀김_조리중.getId(),
                주문_후라이드_한마리_감자튀김_조리중.getOrderTableId(),
                주문_후라이드_한마리_감자튀김_조리중.getOrderStatus(),
                주문_후라이드_한마리_감자튀김_조리중.getOrderedTime(),
                주문_후라이드_한마리_감자튀김_조리중.getOrderLineItems()
        );
        Order argumentOrder = new Order(
                null,
                null,
                OrderStatus.MEAL.name(),
                null,
                null
        );

        // when
        when(orderDao.findById(givenOrder.getId())).thenReturn(Optional.of(givenOrder));
        givenOrder.setOrderStatus(OrderStatus.MEAL.name());
        when(orderDao.save(givenOrder)).thenReturn(givenOrder);
        for (OrderLineItem orderLineItem : givenOrder.getOrderLineItems()) {
            when(orderLineItemDao.findAllByOrderId(orderLineItem.getOrderId())).thenReturn(asList(orderLineItem));
        }

        // then
        assertDoesNotThrow(() -> orderService.changeOrderStatus(givenOrder.getId(), argumentOrder));
    }
}