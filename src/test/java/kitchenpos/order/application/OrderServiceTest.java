package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
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
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
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
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
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
        주문 = createOrder(1L, 주문테이블, null, Lists.newArrayList(주문항목));
    }

    @DisplayName("주문 생성 테스트")
    @Test
    void create() {
        주문항목.setOrderId(주문.id());
        given(menuRepository.countByIdIn(Lists.newArrayList(주문항목.getMenuId()))).willReturn(주문항목.getMenuId());
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        given(orderRepository.save(주문)).willReturn(주문);
        given(orderLineItemDao.save(주문항목)).willReturn(주문항목);
        Order order = orderService.create(주문);
        assertAll(
                () -> assertThat(order.orderTable()).isEqualTo(주문테이블),
                () -> assertThat(order.orderStatus()).isEqualTo(COOKING),
                () -> assertThat(order.orderLineItems().readOnlyOrderLineItems()).containsExactlyElementsOf(Lists.newArrayList(주문항목))
        );
    }

    @DisplayName("주문 생성시 주문항목이 없는 경우 테스트")
    @Test
    void createWithOrderLineItemsEmpty() {
        주문.setOrderLineItems(OrderLineItems.of(new ArrayList<>()));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 생성시 주문에 속하는 주문항목 수와 등록된 메뉴들 중 주문에 속한 주문항목의 메뉴들을 실제 조회했을 때 수가 불일치하는 경우 테스트")
    @Test
    void createNotEqualOrderLineItemsSize() {
        given(menuRepository.countByIdIn(Lists.newArrayList(주문항목.getMenuId()))).willReturn(0L);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 생성시 주문테이블이 등록이 안된 경우 테스트")
    @Test
    void createNotFoundOrderTable() {
        given(menuRepository.countByIdIn(Lists.newArrayList(주문항목.getMenuId()))).willReturn(주문항목.getMenuId());
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 생성시 주문테이블이 비어있는 경우 테스트")
    @Test
    void createWithEmptyOrderTable() {
        주문항목.setOrderId(주문.id());
        주문테이블.setEmpty(true);
        given(menuRepository.countByIdIn(Lists.newArrayList(주문항목.getMenuId()))).willReturn(주문항목.getMenuId());
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 목록 조회 테스트")
    @Test
    void list() {
        given(orderRepository.findAll()).willReturn(Lists.newArrayList(주문));
        given(orderLineItemDao.findAllByOrderId(주문.id())).willReturn(Lists.newArrayList(주문항목));
        List<Order> orders = orderService.list();
        assertThat(orders).containsExactlyElementsOf(Lists.newArrayList(주문));
    }

    @DisplayName("주문 상태 변경 테스트")
    @Test
    void changeOrderStatus() {
        Order order = createOrder(1L, null, COMPLETION, null);
        given(orderRepository.findById(주문.id())).willReturn(Optional.ofNullable(주문));
        given(orderRepository.save(주문)).willReturn(주문);
        Order savedOrder = orderService.changeOrderStatus(주문.id(), order);
        assertThat(savedOrder.orderStatus()).isEqualTo(order.orderStatus());
    }

    @DisplayName("주문 상태 변경시 주문 조회안되는 경우 테스트")
    @Test
    void changeOrderStatusWithNotFoundOrder() {
        Order order = createOrder(1L, null, COMPLETION, null);
        given(orderRepository.findById(주문.id())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.id(), order));
    }

    @DisplayName("주문 상태 변경시 이미 완료인 경우 테스트")
    @Test
    void changeOrderStatusButAlreadyComplete() {
        주문.setOrderStatus(COMPLETION);
        Order order = createOrder(1L, null, COOKING, null);
        given(orderRepository.findById(주문.id())).willReturn(Optional.ofNullable(주문));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.id(), order));
    }
}
