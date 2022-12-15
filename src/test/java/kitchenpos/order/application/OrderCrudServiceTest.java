package kitchenpos.order.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
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
import java.util.List;

import static kitchenpos.order.application.OrderCrudService.ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.order.application.OrderCrudService.ORDERLINEITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("OrderCrudService")
class OrderCrudServiceTest extends ServiceTest {

    @Autowired
    private OrderCrudService orderCrudService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private Long orderTableId;
    private Menu menu;

    @BeforeEach
    void setUp() {

        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A"));
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(null, new Product(new Name("A"), new Price(BigDecimal.valueOf(2))), 1L));
        menu = menuRepository.save(new Menu(new Name("A"), new Price(BigDecimal.valueOf(2)), menuGroup.getId(), menuProducts));

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable());
        orderTables.add(new OrderTable());
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(orderTables));
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        orderTable.setTableGroupId(tableGroup.getId());

        OrderTable orderTable1 = orderTableDao.save(orderTable);
        orderTableId = orderTable1.getId();
        orderCrudService = new OrderCrudService(menuRepository, orderRepository, orderLineItemRepository, orderTableDao);
    }

    @DisplayName("주문을 생성한다. / 주문 항목이 비어있을 수 없다.")
    @Test
    void create_fail_orderLineItems() {

        OrderCreateRequest request = new OrderCreateRequest(orderTableId, new ArrayList<>());

        assertThatThrownBy(() -> orderCrudService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다. / 주문 항목의 수와 메뉴의 수는 같아야 한다.")
    @Test
    void create_fail_orderLineItemSize() {

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, 100L, 1));

        assertThatThrownBy(() -> orderCrudService.create(new OrderCreateRequest(1L, orderLineItems)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDERLINEITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다. / 주문 테이블은 비어있을 수 없다.")
    @Test
    void create_fail_orderTableEmpty() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 3));

        OrderCreateRequest request = new OrderCreateRequest(1L, orderLineItems);

        assertThatThrownBy(() -> orderCrudService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 3));

        OrderResponse orderResponse = orderCrudService.create(new OrderCreateRequest(orderTableId, orderLineItems));

        assertAll(
                () -> assertThat(orderResponse.getStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderedTime()).isNotNull(),
                () -> assertThat(orderResponse.getOrderTableId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderLineItmes()).isNotNull()
        );
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 3));
        orderCrudService.create(new OrderCreateRequest(orderTableId, orderLineItems));
        assertThat(orderCrudService.list()).hasSize(1);
    }
}
