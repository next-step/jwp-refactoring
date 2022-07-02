package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table_group.domain.TableGroup;

public interface TableValidator {

    void checkValidChangeEmpty(OrderTable orderTable);

    void checkValidUngroup(TableGroup tableGroup);
}
