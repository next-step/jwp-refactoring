package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
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

    private Product 치킨;
    private Product 스파게티;
    private MenuProduct 치킨_두마리;
    private MenuProduct 스파게티_이인분;
    private Menu 치킨_스파게티_더블세트_메뉴;
    private OrderTable orderTable;
    private OrderLineItem 주문_항목;
    private Order order;
    private List<Long> menuIds;

    @BeforeEach
    void setUp() {
        치킨 = new Product(1L, "치킨", BigDecimal.valueOf(20_000));
        스파게티 = new Product(2L, "스파게티", BigDecimal.valueOf(10_000));
        치킨_두마리 = new MenuProduct(1L, 1L, 1L, 10);
        스파게티_이인분 = new MenuProduct(2L, 1L, 2L, 10);
        치킨_스파게티_더블세트_메뉴 = new Menu(1L, "치킨 스파게티 더블세트 메뉴", new BigDecimal(13_000), 1L, Arrays.asList(치킨_두마리, 스파게티_이인분));

        orderTable = new OrderTable(1L, null, 0, false);
        order = new Order(1L, orderTable.getId(), null, null, new ArrayList<>());
        주문_항목 = new OrderLineItem(1L, null, 치킨_스파게티_더블세트_메뉴.getId(), 1);
        order.setOrderLineItems(Collections.singletonList(주문_항목));

        menuIds = order.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @Test
    void 주문을_등록할_수_있다() {
        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(주문_항목)).willReturn(주문_항목);

        Order savedOrder = orderService.create(order);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderLineItems()).contains(주문_항목),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull()
        );
    }

    @Test
    void 주문_항목이_비어있는_경우_주문을_등록할_수_없다() {
        Order order = new Order(1L, orderTable.getId(), null, null, null);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록되지_않은_주문_항목이_존재_할_경우_주문을_등록할_수_없다() {
        given(menuDao.countByIdIn(menuIds)).willReturn(0L);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_등록되어_있지_않은_경우_주문을_등록할_수_없다() {
        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있는_경우_주문을_등록할_수_없다() {
        orderTable.setEmpty(true);
        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록을_조회할_수_있다() {
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(Arrays.asList(주문_항목));

        List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
        assertThat(orders).contains(order);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        String expectedOrderStatus = OrderStatus.MEAL.name();
        Order expectedOrder = new Order(order.getId(), orderTable.getId(), expectedOrderStatus, order.getOrderedTime(), order.getOrderLineItems());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        orderService.changeOrderStatus(order.getId(), expectedOrder);

        assertThat(order.getOrderStatus()).isEqualTo(expectedOrderStatus);
    }

    @Test
    void 등록되지_않은_주문_상태를_변경할_수_없다() {
        given(orderDao.findById(order.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 계산_완료된_주문은_상태를_변경할_수_없다() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
