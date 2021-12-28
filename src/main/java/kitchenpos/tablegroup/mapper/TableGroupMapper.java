package kitchenpos.tablegroup.mapper;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

import java.util.stream.Collectors;

public class TableGroupMapper {

    private TableGroupMapper() {
    }

    public static TableGroupResponse toTableGroupResponse(TableGroup tableGroup, TableGroupCreateRequest request) {
        return new TableGroupResponse(tableGroup.getId(), request.getOrderTables()
                .stream()
                .map(TableGroupCreateRequest.OrderTable::getId)
                .map(TableGroupResponse.OrderTable::new)
                .collect(Collectors.toList()));
    }
}
