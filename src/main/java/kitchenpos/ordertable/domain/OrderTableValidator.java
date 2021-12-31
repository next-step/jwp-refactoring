package kitchenpos.ordertable.domain;

import kitchenpos.common.exception.NotEmptyOrderTableStatusException;
import kitchenpos.common.exception.OrderStatusNotProcessingException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class OrderTableValidator {
    private final TableGroupService tableGroupService;

    public OrderTableValidator(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    public void validateChangeableEmpty(OrderTable orderTable) {
        validateNotHavingTableGroup(orderTable);
        validateNotProcessing(orderTable);
    }

    private void validateNotHavingTableGroup(OrderTable orderTable) {
        TableGroup tableGroup = tableGroupService.findByTableGroupId(orderTable.getTableGroupId());

        if (Objects.nonNull(tableGroup)) {
            throw new NotEmptyOrderTableStatusException();
        }
    }

    public void validateNotProcessing(OrderTable orderTable) {
        List<Order> orders = orderTable.getOrders();
        for (Order order : orders) {
            validateNotProcessingWhenChangeEmpty(order);
        }
    }

    public void validateNotProcessingWhenChangeEmpty(Order order) {
        OrderStatus orderStatus = order.getOrderStatus();
        if (!OrderStatus.isMeal(orderStatus) && !OrderStatus.isCooking(orderStatus)) {
            return;
        }

        throw new OrderStatusNotProcessingException();
    }
}
