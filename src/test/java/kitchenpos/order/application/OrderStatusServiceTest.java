package kitchenpos.order.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.Quantity;
import kitchenpos.common.fixture.PriceFixture;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.domain.fixture.MenuProductFixture;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static kitchenpos.common.fixture.NameFixture.nameMenuA;
import static kitchenpos.common.fixture.NameFixture.nameMenuGroupA;
import static kitchenpos.order.application.OrderService.COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.fixture.OrderTableFixture.emptyOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 상태 서비스")
class OrderStatusServiceTest extends ServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;


    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderService orderService;

    private Orders order;

    @BeforeEach
    public void setUp() {
        super.setUp();
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(nameMenuGroupA()));
        Menu menu = menuRepository.save(new Menu(nameMenuA(), PriceFixture.priceMenuA(), menuGroup, singletonList(MenuProductFixture.menuProductA())));

        OrderTable orderTable1 = orderTableRepository.save(emptyOrderTable());
        OrderTable orderTable2 = orderTableRepository.save(emptyOrderTable());

        List<OrderTable> orderTables = new ArrayList<>();

        orderTable1.empty();
        orderTable2.empty();

        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));

        orderTable1.setTableGroup(tableGroup);
        orderTable2.setTableGroup(tableGroup);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        createOrder(orderTable1, menu);
        orderService = new OrderService(menuRepository, orderRepository, orderTableRepository);
    }

    @DisplayName("주문상태를 식사중으로 변경한다.")
    @Test
    void statusMeal_success() {

        OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL);

        assertThat(orderService.changeOrderStatus(order.getId(), request).getOrderStatus())
                .isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문완료일 경우 주문상태를 변경할 수 없다.")
    @Test
    void changeStatus_fail() {

        OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.COMPLETION);

        assertThat(orderService.changeOrderStatus(order.getId(), request).getOrderStatus())
                .isEqualTo(OrderStatus.COMPLETION);

        주문완료_검증됨();

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문상태를 완료로 변경한다.")
    @Test
    void name() {
        orderService.changeOrderStatus(order.getId(), new OrderStatusChangeRequest(OrderStatus.COMPLETION));
        주문완료_검증됨();
    }

    private void 주문완료_검증됨() {
        Orders findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    private void createOrder(OrderTable orderTable1, Menu menu) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(singletonList(new OrderLineItem(null, menu.getId(), new Quantity(1))));
        orderTable1.setEmpty(false);
        order = orderRepository.save(new Orders(orderTable1, orderLineItems));
    }
}
