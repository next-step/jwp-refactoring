package kitchenpos.domain.tablegroup.validator;

import java.util.List;

public interface TableGroupUnGroupValidator {
    void validate(List<Long> orderTableIds);
}
