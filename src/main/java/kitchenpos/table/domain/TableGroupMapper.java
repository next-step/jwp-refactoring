package kitchenpos.table.domain;

import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.TableGroupRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableGroupMapper {
    private final OrderTableRepository orderTableRepository;

    public TableGroupMapper(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public TableGroup mapFrom(TableGroupRequest request) {
        if (request.getOrderTables() == null || request.getOrderTables().size() < 2) {
            throw new IllegalArgumentException();
        }

        OrderTables orderTables = getOrderTables(request);

        if (request.getOrderTables().size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        if (orderTables.existsEmptyTable()) {
            throw new IllegalArgumentException();
        }

        if (orderTables.existsTableGroup()) {
            throw new IllegalArgumentException();
        }

        return new TableGroup(orderTables);
    }

    private OrderTables getOrderTables(TableGroupRequest request) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(groupIds(request));
        return new OrderTables(orderTables);
    }

    private List<Long> groupIds(TableGroupRequest request) {
        return request.getOrderTables()
                .stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
