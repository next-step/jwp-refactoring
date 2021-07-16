package kitchenpos;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupDomainService;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderTestSupport extends MenuTestSupport {
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;

    public OrderTable 테이블_등록되어있음(int numberOfGuests, boolean empty) {
        return orderTableRepository.save(new OrderTable(numberOfGuests, empty));
    }

    public Order 주문_등록되어있음(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return orderRepository.save(new Order(orderTable, orderLineItems, new OrderValidator(menuRepository)));
    }

    public TableGroup 단체지정_등록되어있음(List<OrderTable> orderTables) {
        TableGroupDomainService tableGroupDomainService = new TableGroupDomainService();
        return tableGroupRepository.save(tableGroupDomainService.group(orderTables));
    }
}
