package kitchenpos.mapper;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupResponse;

import java.util.stream.Collectors;

public class TableGroupMapper {

    private TableGroupMapper() {
    }

    public static TableGroupResponse toTableGroupResponse(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .map(TableGroupResponse.OrderTable::new)
                .collect(Collectors.toList()));
    }
}
