package kitchenpos.tablegroup.domain;

import java.util.List;

public interface TableGroupLinker {
    void validateOrderStatusByTableIds(List<Long> orderTableIds);
}
