package kitchenpos.order.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.product.domain.ProductFixture;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.application.OrderStatusService.COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 상태 서비스")
class OrderStatusServiceTest extends ServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableDao orderTableDao;


    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderStatusService orderStatusService;

    private Order order;

    @BeforeEach
    void setUp() {

        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("a"));
        Menu menu = menuRepository.save(new Menu(new Name("menu"), new Price(BigDecimal.ONE), menuGroup.getId(), Arrays.asList(new MenuProduct(null, ProductFixture.product(), 1L))));

        OrderTable orderTable1 = orderTableDao.save(new OrderTable());
        OrderTable orderTable2 = orderTableDao.save(new OrderTable());

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        TableGroup tableGroup = tableGroupDao.save(new TableGroup(orderTables));

        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable2.setTableGroupId(tableGroup.getId());
        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);

        createOrder(orderTable1, menu);

        orderStatusService = new OrderStatusService(orderRepository, orderLineItemRepository);
    }

    private void createOrder(OrderTable orderTable1, Menu menu) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 1));
        order = orderRepository.save(new Order(orderTable1.getId(), orderLineItems));
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

        주문완료_검증됨();

        assertThatThrownBy(() -> orderStatusService.changeOrderStatus(order.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문상태를 완료로 변경한다.")
    @Test
    void name() {
        orderStatusService.changeOrderStatus(order.getId(), new OrderStatusChangeRequest(OrderStatus.COMPLETION));
        주문완료_검증됨();
    }

    private void 주문완료_검증됨() {
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}
