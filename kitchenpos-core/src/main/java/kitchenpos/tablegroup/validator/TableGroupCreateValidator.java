package kitchenpos.tablegroup.validator;

import java.util.List;

public interface TableGroupCreateValidator {
    void validate(List<Long> orderTableIds);
}
