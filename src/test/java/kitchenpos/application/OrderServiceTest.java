package kitchenpos.application;

import static kitchenpos.utils.DomainFixtureFactory.createOrder;
import static kitchenpos.utils.DomainFixtureFactory.createOrderLineItem;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
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
import org.assertj.core.util.Lists;
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
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private OrderService orderService;

    private OrderTable 주문테이블;
    private OrderLineItem 주문항목;
    private Order 주문;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, null, 2, false);
        주문항목 = createOrderLineItem(1L, null, 1L, 2L);
        주문 = createOrder(1L, 주문테이블.getId(), null, null, Lists.newArrayList(주문항목));
    }

    @DisplayName("주문 생성 테스트")
    @Test
    void create() {
        주문항목.setOrderId(주문.getId());
        given(menuDao.countByIdIn(Lists.newArrayList(주문항목.getMenuId()))).willReturn(주문항목.getMenuId());
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        given(orderDao.save(주문)).willReturn(주문);
        given(orderLineItemDao.save(주문항목)).willReturn(주문항목);
        Order order = orderService.create(주문);
        assertAll(
                () -> assertThat(order.getOrderTableId()).isEqualTo(주문테이블.getId()),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(order.getOrderLineItems()).containsExactlyElementsOf(Lists.newArrayList(주문항목))
        );
    }

    @DisplayName("주문 생성시 주문항목이 없는 경우 테스트")
    @Test
    void createWithOrderLineItemsEmpty() {
        주문.setOrderLineItems(new ArrayList<>());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 생성시 주문에 속하는 주문항목 수와 등록된 메뉴들 중 주문에 속한 주문항목의 메뉴들을 실제 조회했을 때 수가 불일치하는 경우 테스트")
    @Test
    void createNotEqualOrderLineItemsSize() {
        given(menuDao.countByIdIn(Lists.newArrayList(주문항목.getMenuId()))).willReturn(0L);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 생성시 주문테이블이 등록이 안된 경우 테스트")
    @Test
    void createNotFoundOrderTable() {
        given(menuDao.countByIdIn(Lists.newArrayList(주문항목.getMenuId()))).willReturn(주문항목.getMenuId());
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 생성시 주문테이블이 비어있는 경우 테스트")
    @Test
    void createWithEmptyOrderTable() {
        주문항목.setOrderId(주문.getId());
        주문테이블.setEmpty(true);
        given(menuDao.countByIdIn(Lists.newArrayList(주문항목.getMenuId()))).willReturn(주문항목.getMenuId());
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 목록 조회 테스트")
    @Test
    void list() {
        given(orderDao.findAll()).willReturn(Lists.newArrayList(주문));
        given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(Lists.newArrayList(주문항목));
        List<Order> orders = orderService.list();
        assertThat(orders).containsExactlyElementsOf(Lists.newArrayList(주문));
    }

    @DisplayName("주문 상태 변경 테스트")
    @Test
    void changeOrderStatus() {
        Order order = createOrder(1L, null, OrderStatus.COMPLETION.name(), null, null);
        given(orderDao.findById(주문.getId())).willReturn(Optional.ofNullable(주문));
        given(orderDao.save(주문)).willReturn(주문);
        Order savedOrder = orderService.changeOrderStatus(주문.getId(), order);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @DisplayName("주문 상태 변경시 주문 조회안되는 경우 테스트")
    @Test
    void changeOrderStatusWithNotFoundOrder() {
        Order order = createOrder(1L, null, OrderStatus.COMPLETION.name(), null, null);
        given(orderDao.findById(주문.getId())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.getId(), order));
    }

    @DisplayName("주문 상태 변경시 이미 완료인 경우 테스트")
    @Test
    void changeOrderStatusButAlreadyComplete() {
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        Order order = createOrder(1L, null, OrderStatus.COOKING.name(), null, null);
        given(orderDao.findById(주문.getId())).willReturn(Optional.ofNullable(주문));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.getId(), order));
    }
}
