package kitchenpos.order.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.order.application.OrderStatusService.COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 상태 서비스")
class OrderStatusServiceTest extends ServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;


    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderStatusService orderStatusService;

    private Order order;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("a"));
        List<MenuProduct> menuProducts = new ArrayList<>();
        OrderTable orderTable1 = orderTableDao.save(new OrderTable());
        OrderTable orderTable2 = orderTableDao.save(new OrderTable());
        Menu menu = menuDao.save(new Menu("menu", BigDecimal.ONE, menuGroup.getId()));
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(orderTables));
        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable2.setTableGroupId(tableGroup.getId());
        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 1));
        order = orderDao.save(new Order(orderTable1.getId(), orderLineItems));
        orderStatusService = new OrderStatusService(orderDao, orderLineItemDao);
    }

    @DisplayName("주문상태를 식사중으로 변경한다.")
    @Test
    void statusMeal_success() {

        OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL);

        assertThat(orderStatusService.changeOrderStatus(order.getId(), request).getOrderStatus())
                .isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문완료일 경우 주문상태를 변경할 수 없다.")
    @Test
    void changeStatus_fail() {

        OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.COMPLETION);

        assertThat(orderStatusService.changeOrderStatus(order.getId(), request).getOrderStatus())
                .isEqualTo(OrderStatus.COMPLETION.name());

        Order order1 = orderDao.findById(order.getId()).get();

        assertThat(order1.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> orderStatusService.changeOrderStatus(order.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문상태를 완료로 변경한다.")
    @Test
    void name() {
        orderStatusService.changeOrderStatus(order.getId(), new OrderStatusChangeRequest(OrderStatus.COMPLETION));
        Order order1 = orderDao.findById(order.getId()).get();
        assertThat(order1.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}
