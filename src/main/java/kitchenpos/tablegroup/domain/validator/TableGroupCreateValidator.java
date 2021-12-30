package kitchenpos.tablegroup.domain.validator;

import java.util.List;

public interface TableGroupCreateValidator {
    void validate(List<Long> orderTableIds);
}
