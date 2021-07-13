package kitchenpos.table.domain;

import java.util.List;

public interface TableDependencyHelper {
    boolean existsByOrderTableIdAndOrderStatusNotCompletion(Long orderTableId);

    boolean existsByOrderTableIdInAndOrderStatusNotCompletion(List<Long> orderTableId);
}
