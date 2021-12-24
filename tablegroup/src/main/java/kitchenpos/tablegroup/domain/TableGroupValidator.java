package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.dto.TableGroupDto;

public interface TableGroupValidator {
    public void validateForUnGroup(OrderTables orderTables);

    public OrderTables getComplateOrderTable(Long tableGroupId);

    public OrderTables getValidatedOrderTables(TableGroupDto tableGroup);
}
