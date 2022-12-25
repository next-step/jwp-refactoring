package kitchenpos.table.validator;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupValidator {

    private static final int MIN_NUMBER_OF_GROUP = 2;

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderTableRepository orderTableRepository,
        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public void validateTableGroup(TableGroupRequest request) {
        validateOrderTableExist(request.getOrderTableIds());
    }

    private void validateOrderTableExist(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("단체 지정할 테이블 중 존재하지 않는 테이블이 존재 합니다.");
        }

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_NUMBER_OF_GROUP) {
            throw new IllegalArgumentException("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만 입니다.");
        }

        orderTables.stream()
            .filter(orderTable -> !orderTable.ableToGroup())
            .findFirst()
            .ifPresent(o -> {
                    throw new IllegalArgumentException("테이블이 비어있지 않거나, 이미 단체 지정된 테이블 입니다.");
                }
            );
    }

    public void validateUnGroup(TableGroup tableGroup) {
        List<OrderTable> orderTables = orderTableRepository
            .findAllByTableGroupId(tableGroup.getId());
        List<Order> orders = findAllOrderIds(orderTables);

        orders.stream()
            .filter(Order::onCookingOrMeal)
            .findFirst()
            .ifPresent(o -> {
                throw new IllegalArgumentException("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
            });
    }

    private List<Order> findAllOrderIds(List<OrderTable> orderTables) {
        List<Long> orderTablesIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        return orderRepository.findAllByOrderTableIdIn(orderTablesIds);
    }
}
