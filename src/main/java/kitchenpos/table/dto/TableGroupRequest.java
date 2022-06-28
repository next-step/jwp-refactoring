package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableIdRequest> tableGroups = new ArrayList<>();

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.tableGroups = orderTables;
    }

    public TableGroup toTableGroup() {
        return new TableGroup();
    }

    public List<Long> getGroupIds() {
        if (CollectionUtils.isEmpty(tableGroups) || tableGroups.size() < 2) {
            throw new IllegalArgumentException();
        }
        return tableGroups.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTableIdRequest> getIds() {
        return tableGroups;
    }

    public void validateSize(int orderTableSize) {
        if (tableGroups.size() != orderTableSize) {
            throw new IllegalArgumentException();
        }
    }
}
