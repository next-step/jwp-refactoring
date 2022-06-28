package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.factory.OrderFixture.주문_메뉴_생성;
import static kitchenpos.factory.OrderFixture.주문테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    MenuDao menuDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    Order 내주문;

    OrderTable 주문테이블;

    OrderLineItem 주문상품1;
    OrderLineItem 주문상품2;

    @Test
    @DisplayName("주문을 생성한다 (Happy Path)")
    void create() {
        //given
        내주문 = new Order(1L, 주문테이블.getId());
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문.getId(), 2L);
        내주문.setOrderLineItems(Arrays.asList(주문상품1, 주문상품2));
        given(menuDao.countByIdIn(anyList())).willReturn(Long.valueOf(내주문.getOrderLineItems().size()));
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderDao.save(any(Order.class))).willReturn(내주문);
        given(orderLineItemDao.save(주문상품1)).willReturn(주문상품1);
        given(orderLineItemDao.save(주문상품2)).willReturn(주문상품2);

        //when
        Order savedOrder = orderService.create(내주문);

        //then
        assertAll(() -> {
            assertThat(savedOrder.getOrderLineItems().stream()
                    .map(OrderLineItem::getMenuId)
                    .collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(주문상품1.getMenuId(), 주문상품2.getMenuId()));
            assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(savedOrder.getOrderTableId()).isEqualTo(주문테이블.getId());
        });
    }

    @Test
    @DisplayName("주문 메뉴가 없을 경우 주문 생성 불가")
    void createEmptyOrderLineItems() {
        //given
        내주문 = new Order(1L, 주문테이블.getId());
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문.getId(), 2L);

        //then
        assertThatThrownBy(() -> {
            orderService.create(내주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문에 포함된 주문 메뉴가 유효하지 않은 메뉴가 있을 경우 주문 생성 불가")
    void createInvalidOrderLineItems() {
        //given
        OrderLineItem 유효하지않은메뉴 = new OrderLineItem();
        내주문 = new Order(1L, 주문테이블.getId());
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문.getId(), 2L);
        내주문.setOrderLineItems(Arrays.asList(주문상품1, 주문상품2));
        given(menuDao.countByIdIn(anyList())).willReturn(Long.valueOf(Arrays.asList(주문상품1, 주문상품2, 유효하지않은메뉴).size()));

        //then
        assertThatThrownBy(() -> {
            orderService.create(내주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 유효하지 않을 경우 주문 생성 불가")
    void createInvalidOrderTable() {
        //given
        내주문 = new Order(1L, 주문테이블.getId());
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문.getId(), 2L);
        내주문.setOrderLineItems(Arrays.asList(주문상품1, 주문상품2));
        given(menuDao.countByIdIn(anyList())).willReturn(Long.valueOf(내주문.getOrderLineItems().size()));
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            orderService.create(내주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문한 주문 테이블이 비어있는 경우 주문 생성 불가")
    void createIsEmptyOrderTable() {
        //given
        내주문 = new Order(1L, 주문테이블.getId());
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문.getId(), 2L);
        내주문.setOrderLineItems(Arrays.asList(주문상품1, 주문상품2));
        given(menuDao.countByIdIn(anyList())).willReturn(Long.valueOf(내주문.getOrderLineItems().size()));
        주문테이블.setEmpty(true);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문테이블));

        //then
        assertThatThrownBy(() -> {
            orderService.create(내주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문리스트를 조회한다 (Happy Path)")
    void list() {
        //given
        내주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(주문상품1, 주문상품2));
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문.getId(), 2L);
        given(orderDao.findAll()).willReturn(Arrays.asList(내주문));
        given(orderLineItemDao.findAllByOrderId(내주문.getId())).willReturn(Arrays.asList(주문상품1, 주문상품2));

        //when
        List<Order> orders = orderService.list();

        //then
        assertAll(() -> {
            assertThat(orders).containsExactlyInAnyOrderElementsOf(Arrays.asList(내주문));
            assertThat(orders.stream()
                            .map(Order::getOrderLineItems)
                            .collect(Collectors.toList()))
                            .containsExactlyInAnyOrderElementsOf(Arrays.asList(Arrays.asList(주문상품1, 주문상품2)));
            assertThat(orders.stream()
                    .map(Order::getOrderStatus)
                    .collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(OrderStatus.COOKING.name()));
            assertThat(orders.stream()
                    .map(Order::getOrderTableId)
                    .collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(주문테이블.getId()));
        });
    }

    @Test
    @DisplayName("주문상태를 변경한다 (Happy Path)")
    void changeOrderStatus() {
        //given
        내주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(주문상품1, 주문상품2));
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문.getId(), 2L);
        Order 변경주문 = new Order();
        변경주문.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(anyLong())).willReturn(Optional.of(내주문));
        given(orderDao.save(any(Order.class))).willReturn(내주문);
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(내주문.getOrderLineItems());

        //when
        Order changedOrder = orderService.changeOrderStatus(내주문.getId(), 변경주문);

        //then
        assertThat(changedOrder.getId()).isEqualTo(내주문.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(변경주문.getOrderStatus());
    }

    @Test
    @DisplayName("변경하려는 주문이 유효하지 않을 때 주문상태 변경불가")
    void changeOrderStatusInvalidOrder() {
        //given
        내주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(주문상품1, 주문상품2));
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문.getId(), 2L);
        Order 변경주문 = new Order();
        변경주문.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(내주문.getId(), 변경주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("변경하려는 주문이 완료 상태일 때 주문상태 변경불가")
    void changeOrderStatusAlreadyCompleted() {
        //given
        내주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(주문상품1, 주문상품2));
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문.getId(), 2L);
        Order 변경주문 = new Order();
        변경주문.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(anyLong())).willReturn(Optional.of(내주문));

        //then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(내주문.getId(), 변경주문);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}