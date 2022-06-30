package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;

public class TableGroupFactory {
    public static TableGroupRequest createTableGroupRequest(List<Long> orderIds) {
        return new TableGroupRequest(orderIds.stream()
                .map(TableGroupFactory::createOrderTableIdRequest)
                .collect(Collectors.toList()));
    }

    public static OrderTableIdRequest createOrderTableIdRequest(Long id) {
        return new OrderTableIdRequest(id);
    }
}
