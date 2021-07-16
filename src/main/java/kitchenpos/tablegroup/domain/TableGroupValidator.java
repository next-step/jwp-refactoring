package kitchenpos.tablegroup.domain;

import java.util.List;

public interface TableGroupValidator {
    void validateUngrouping(TableGroup tableGroup);
    void validateGrouping(List<Long> orderTableIds);
}
