package kitchenpos.event.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.event.table.TableGroupCreatedEvent;
import kitchenpos.exception.order.InvalidOrderLineItemsException;
import kitchenpos.repository.menu.MenuRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderEventHandler {

    private final MenuRepository menuRepository;

    public OrderEventHandler(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOrder(OrderCreatedEvent event) {
        Order order = event.getOrder();

        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        int size = orderLineItems.size();
        long savedMenuCount = menuRepository.countByIdIn(menuIds);
        if (size != savedMenuCount) {
            throw new InvalidOrderLineItemsException("orderLineItems size: " + size +
                    "saved orderLineItems size: " + savedMenuCount);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createdTableGroup(TableGroupCreatedEvent event) {
        TableGroup tableGroup = event.getTableGroup();
        tableGroup.initialSettingOrderTables(tableGroup.getOrderTables());
    }
}
