package kitchenpos.order.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.table.dao.OrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static kitchenpos.order.application.OrderCrudService.ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderCrudService")
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

        OrderCreateRequest request = new OrderCreateRequest();

        assertThatThrownBy(() -> orderCrudService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE);
    }
}
