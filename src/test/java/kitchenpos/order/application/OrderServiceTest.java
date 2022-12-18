package kitchenpos.order.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
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

import static kitchenpos.menu.domain.fixture.MenuFixture.menuA;
import static kitchenpos.menu.domain.fixture.MenuGroupFixture.menuGroupA;
import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.fixture.OrderTableFixture.emptyOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("OrderCrudService")
class OrderServiceTest extends ServiceTest {

    public static final long NOT_EXIST_ORDER_TABLE_ID = 100L;
    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private OrderTable orderTableA;
    private Menu menu;

    @BeforeEach
    public void setUp() {
        super.setUp();
        MenuGroup menuGroup = menuGroupRepository.save(menuGroupA());
        menu = menuRepository.save(menuA());

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(emptyOrderTable());
        orderTables.add(emptyOrderTable());
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        orderTableA = orderTableRepository.save(new OrderTable());
        orderTableA.setTableGroup(tableGroup);

        orderTableA = orderTableRepository.save(orderTableA);
        orderService = new OrderService(menuRepository, orderRepository, orderTableRepository);
    }

    @DisplayName("주문을 생성한다. / 주문 항목이 비어있을 수 없다.")
    @Test
    void create_fail_orderLineItems() {

        OrderCreateRequest request = new OrderCreateRequest(orderTableA.getId(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다. / 주문 항목의 수와 메뉴의 수는 같아야 한다.")
    @Test
    void create_fail_orderLineItemSize() {
        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(orderTableA.getId(), notExistMenuOrderLineItem())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다. / 주문 테이블은 비어있을 수 없다.")
    @Test
    void create_fail_orderTableEmpty() {

        OrderCreateRequest request = new OrderCreateRequest(NOT_EXIST_ORDER_TABLE_ID, orderLineItemsA());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {

        OrderResponse orderResponse = orderService.create(new OrderCreateRequest(orderTableA.getId(), orderLineItemsA()));

        assertAll(
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderedTime()).isNotNull(),
                () -> assertThat(orderResponse.getOrderTableId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderLineItems()).isNotNull()
        );
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        orderService.create(new OrderCreateRequest(orderTableA.getId(), orderLineItemsA()));
        assertThat(orderService.list()).hasSize(1);
    }

    private List<OrderLineItem> orderLineItemsA() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), new Quantity(3)));
        return orderLineItems;
    }

    private List<OrderLineItem> notExistMenuOrderLineItem() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), new Quantity(3)));
        orderLineItems.add(new OrderLineItem(null, 30L, new Quantity(3)));
        return orderLineItems;
    }
}
