package kitchenpos.tablegroup.domain.validator;

import java.util.List;

public interface CreateTableGroupValidator {
    void validate(List<Long> orderTableIds);
}
