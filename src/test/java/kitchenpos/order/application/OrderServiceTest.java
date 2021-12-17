package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.OrderService;
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

@DisplayName("주문 서비스 테스트")
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

    private final Menu menu = new Menu();
    private final OrderLineItem orderLineItem = new OrderLineItem();
    private final OrderTable orderTable = new OrderTable();
    private final Order order = new Order();

    @BeforeEach
    void setUp() {
        menu.setId(1L);
        menu.setName("후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(10000));

        orderLineItem.setQuantity(2);
        orderLineItem.setMenuId(menu.getId());

        orderTable.setId(1L);
        orderTable.setEmpty(false);

        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderTableId(orderTable.getId());
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {

        when(menuDao.countByIdIn(anyList()))
            .thenReturn(1L);
        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(orderTable));
        when(orderDao.save(any(Order.class)))
            .thenReturn(order);
        when(orderLineItemDao.save(any(OrderLineItem.class)))
            .thenReturn(orderLineItem);

        Order saved = orderService.create(order);

        assertAll(() -> {
            assertNotNull(saved.getOrderedTime());
            assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        });
    }

    @Test
    @DisplayName("빈 테이블은 주문을 생성할 수 없다.")
    void createValidateEmptyTable() {
        orderTable.setEmpty(true);

        when(menuDao.countByIdIn(anyList()))
            .thenReturn(1L);
        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴로 등록된 메뉴만 선택할 수 있다.")
    void createValidateMenu() {
        when(menuDao.countByIdIn(anyList()))
            .thenReturn(0L);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴는 하나 이상 선택해야 한다.")
    void createValidateOrderLineItems() {
        order.setOrderLineItems(new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        order.setId(1L);
        when(orderDao.findAll())
            .thenReturn(Arrays.asList(order));
        when(orderLineItemDao.findAllByOrderId(anyLong()))
            .thenReturn(Arrays.asList(orderLineItem));

        List<Order> orders = orderService.list();

        assertAll(() -> {
            assertThat(orders.size()).isEqualTo(1);
            assertThat(orders.get(0).getOrderLineItems())
                .extracting(OrderLineItem::getQuantity).containsExactly(2L);
        });
    }

    @Test
    @DisplayName("주문의 상태를 관리한다")
    void changeOrderStatus() {
        order.setId(1L);
        when(orderDao.findById(anyLong()))
            .thenReturn(Optional.of(order));
        when(orderLineItemDao.findAllByOrderId(anyLong()))
            .thenReturn(Arrays.asList(orderLineItem));

        Order request = new Order();
        request.setOrderStatus(OrderStatus.COMPLETION.name());
        Order changed = orderService.changeOrderStatus(1L, request);

        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("이미 계산완료된 주문은 상태를 변경할 수 없다.")
    void changeOrderStatusValidate() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        when(orderDao.findById(anyLong()))
            .thenReturn(Optional.of(order));

        Order request = new Order();
        request.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, request))
            .isInstanceOf(IllegalArgumentException.class);
    }
}