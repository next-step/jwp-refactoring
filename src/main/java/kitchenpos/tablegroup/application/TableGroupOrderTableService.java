package kitchenpos.tablegroup.application;

import java.util.List;

public interface TableGroupOrderTableService {
    List<Long> findOrderTableIdsByTableGroupId(Long tableGroupId);
}
