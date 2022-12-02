package kitchenpos.order.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.table.dao.OrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.order.application.OrderCrudService.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @BeforeEach
    void setUp() {
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
}
