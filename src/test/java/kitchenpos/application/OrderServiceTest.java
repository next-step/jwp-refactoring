package kitchenpos.application;

import kitchenpos.menu.dao.MenuDao;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 비즈니스 로직을 처리하는 서비스 테스트")
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

    private List<OrderLineItem> orderLineItems;
    private OrderLineItem orderLineItem;
    private Order paramsOrder;
    private Order expectedOrder;
    private OrderTable orderTable;
    private List<Long> menuIds;

    @BeforeEach
    void setUp() {
        orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setSeq(1L);
        orderLineItem.setQuantity(1L);

        orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);

        paramsOrder = new Order();
        paramsOrder.setOrderTableId(orderTable.getId());
        paramsOrder.setOrderStatus(OrderStatus.COOKING.name());
        paramsOrder.setOrderLineItems(orderLineItems);

        expectedOrder = new Order();
        expectedOrder.setId(1L);
        expectedOrder.setOrderTableId(orderTable.getId());
        expectedOrder.setOrderStatus(OrderStatus.COOKING.name());
        expectedOrder.setOrderLineItems(orderLineItems);

        menuIds = paramsOrder.getOrderLineItems()
            .stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        given(menuDao.countByIdIn(menuIds)).willReturn((long) paramsOrder.getOrderLineItems().size());
        given(orderTableDao.findById(paramsOrder.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(paramsOrder)).willReturn(expectedOrder);
        given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);

        final Order createdOrder = orderService.create(paramsOrder);

        assertThat(createdOrder.getId()).isEqualTo(expectedOrder.getId());
        assertThat(createdOrder.getOrderTableId()).isEqualTo(expectedOrder.getOrderTableId());
        assertThat(createdOrder.getOrderLineItems()).containsExactly(orderLineItem);
    }

    @DisplayName("상품을 주문하지 않는 경우 주문을 생성할 수 없다.")
    @Test
    void 상품을_주문하지_않는_경우_주문_생성() {
        paramsOrder.setOrderLineItems(null);

        assertThatThrownBy(() -> {
            final Order createdOrder = orderService.create(paramsOrder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 등록되지 않은 상품을 주문하는 경우 주문을 생성할 수 없다.")
    @Test
    void 등록되지_않은_상품을_주문하는_경우_주문_생성() {
        given(menuDao.countByIdIn(menuIds)).willReturn(0L);

        assertThatThrownBy(() -> {
            final Order createdOrder = orderService.create(paramsOrder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    // 존재하지 않는(비어있는) 주문 테이블이라면 생성 불가
    @DisplayName("존재하지 않는 주문 테이블을 주문하는 경우 주문을 생성할 수 없다.")
    @Test
    void 존재하지_않는_주문_테이블을_주문하는_경우_주문_생성() {
        given(menuDao.countByIdIn(menuIds)).willReturn((long) paramsOrder.getOrderLineItems().size());
        given(orderTableDao.findById(paramsOrder.getOrderTableId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            final Order createdOrder = orderService.create(paramsOrder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void 주문_조회() {
        given(orderDao.findAll()).willReturn(Collections.singletonList(expectedOrder));
        given(orderLineItemDao.findAllByOrderId(expectedOrder.getId())).willReturn(Collections.singletonList(expectedOrder.getOrderLineItems().get(0)));

        final List<Order> orders = orderService.list();

        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).getId()).isEqualTo(expectedOrder.getId());
        assertThat(orders.get(0).getOrderTableId()).isEqualTo(expectedOrder.getOrderTableId());
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(expectedOrder.getOrderStatus());
        assertThat(orders.get(0).getOrderLineItems().get(0).getQuantity()).isEqualTo(expectedOrder.getOrderLineItems().get(0).getQuantity());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void 주문_상태_변경() {
        paramsOrder.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(expectedOrder.getId())).willReturn(Optional.of(expectedOrder));
        given(orderLineItemDao.findAllByOrderId(expectedOrder.getId())).willReturn(Collections.singletonList(orderLineItem));

        final Order changedOrder = orderService.changeOrderStatus(expectedOrder.getId(), paramsOrder);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("없는 주문의 경우 상태를 변경할 수 없다..")
    @Test
    void 주문이_존재하지_않는_경우_주문_상태_변경() {
        paramsOrder.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(expectedOrder.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            final Order createdOrder = orderService.changeOrderStatus(expectedOrder.getId(), paramsOrder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 완료된 상태인 경우 주문의 상태를 변경할 수 없다.")
    @Test
    void 주문이_완료된_상태인_경우_주문_상태_변경() {
        expectedOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderDao.findById(expectedOrder.getId())).willReturn(Optional.of(expectedOrder));

        assertThatThrownBy(() -> {
            final Order createdOrder = orderService.changeOrderStatus(expectedOrder.getId(), paramsOrder);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
