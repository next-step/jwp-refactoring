package kitchenpos.order.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.Quantity;
import kitchenpos.common.fixture.PriceFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static java.util.Collections.singletonList;
import static kitchenpos.common.fixture.NameFixture.nameMenuA;
import static kitchenpos.common.fixture.NameFixture.nameMenuGroupA;
import static kitchenpos.menu.domain.fixture.MenuProductFixture.menuProductA;
import static kitchenpos.order.application.OrderService.COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE;
import static kitchenpos.product.domain.fixture.ProductFixture.createProductA;
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
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    private Orders order;

    @BeforeEach
    public void setUp() {
        super.setUp();
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(nameMenuGroupA()));
        Product product = productRepository.save(createProductA());
        Menu menu = menuRepository.save(new Menu(nameMenuA(), PriceFixture.priceMenuA(), menuGroup, new MenuProducts(singletonList(menuProductA(product)))));
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(emptyOrderTable(), emptyOrderTable()))));
        createOrder(tableGroup.getOrderTables().get(0), menu);
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

    private void createOrder(OrderTable orderTable, Menu menu) {
        orderTable.setEmpty(false);
        order = orderRepository.save(new Orders(orderTable.getId(), new OrderLineItems(singletonList(new OrderLineItem(null, OrderMenu.of(menu.getId(), menu.getName(), menu.getPrice()), new Quantity(1))))));
    }
}
