package kitchenpos.tablegroup.domain;

import java.util.List;

public interface TableGroupManager {
    void grouping(Long tableGroupId, List<Long> orderTableIds);
    void ungrouping(Long tableGroupId);
}
