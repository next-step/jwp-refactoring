package kitchenpos.order.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.order.application.OrderCrudService.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("OrderCrudService")
@SpringBootTest
class OrderCrudServiceTest {

    @Autowired
    private OrderCrudService orderCrudService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private Long orderTableId;

    @BeforeEach
    void setUp() {

        List<OrderTable> orderTables = new ArrayList<>();
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(orderTables));
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        orderTable.setTableGroupId(tableGroup.getId());
        orderTable = orderTableDao.save(orderTable);
        orderCrudService = new OrderCrudService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주문을 생성한다. / 주문 항목이 비어있을 수 없다.")
    @Test
    void create_fail_orderLineItems() {

        OrderCreateRequest request = new OrderCreateRequest(1L, new ArrayList<>());

        assertThatThrownBy(() -> orderCrudService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다. / 주문 항목의 수와 메뉴의 수는 같아야 한다.")
    @Test
    void create_fail_orderLineItemSize() {

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(1L, 100L, 1));

        assertThatThrownBy(() -> orderCrudService.create(new OrderCreateRequest(1L, orderLineItems)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDERLINEITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다. / 주문 테이블은 비어있을 수 없다.")
    @Test
    void create_fail_orderTableEmpty() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(1L, 2L, 3));

        OrderCreateRequest request = new OrderCreateRequest(1L, orderLineItems);

        assertThatThrownBy(() -> orderCrudService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(1L, 2L, 3));

        OrderResponse orderResponse = orderCrudService.create(new OrderCreateRequest(orderTableId, orderLineItems));

        assertAll(
                () -> assertThat(orderResponse.getStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderedTime()).isNotNull(),
                () -> assertThat(orderResponse.getOrderTableId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderLineItmes()).isNotNull()
        );
    }
}
