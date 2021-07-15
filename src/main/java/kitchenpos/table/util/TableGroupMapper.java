package kitchenpos.table.util;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupMapper {

    public TableGroup mapFormToTableGroup(TableGroupRequest tableGroupRequest) {
        OrderTables orderTables = mapFormToOrderTables(tableGroupRequest.getOrderTableRequests());
        return new TableGroup(orderTables);
    }

    private OrderTables mapFormToOrderTables(List<OrderTableRequest> orderTableRequests) {
        List<OrderTable> orderTables = orderTableRequests.stream()
                .map(orderTableRequest -> toOrderTable(orderTableRequest.getId(), orderTableRequest.getNumberOfGuests()))
                .collect(Collectors.toList());
        return new OrderTables(orderTables);
    }

    private OrderTable toOrderTable(Long id, int numberOfGuests) {
        if (id != null) {
            new IllegalArgumentException("주문 테이블 ID는 필수입니다.");
        }
        if(numberOfGuests < 0){
            new IllegalArgumentException("테이블 손님은 0명 이상이어야 합니다.");
        }
        return new OrderTable(id, numberOfGuests);
    }
}
