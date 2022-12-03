package kitchenpos.order.application;

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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 상태 서비스")
@SpringBootTest
class OrderStatusServiceTest {

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
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        Menu menu = menuDao.save(new Menu("menu", BigDecimal.ONE, menuGroup.getId()));
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable);
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(orderTables));
        orderTable.setTableGroupId(tableGroup.getId());
        orderTableDao.save(orderTable);
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 1));
        order = orderDao.save(new Order(orderTable.getId(), orderLineItems));
        orderStatusService = new OrderStatusService(orderDao, orderLineItemDao);
    }

    @DisplayName("주문상태를 식사중으로 변경한다.")
    @Test
    void statusMeal_success() {

        OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL);

        assertThat(orderStatusService.changeOrderStatus(order.getId(), request).getOrderStatus())
                .isEqualTo(OrderStatus.MEAL.name());
    }
}
