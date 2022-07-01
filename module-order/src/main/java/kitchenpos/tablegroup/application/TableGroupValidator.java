package kitchenpos.tablegroup.application;

import java.util.List;


public interface TableGroupValidator {
    void validateComplete(List<Long> orderTableIds);
}
