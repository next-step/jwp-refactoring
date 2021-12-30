package kitchenpos.tablegroup.domain.validator;

import java.util.List;

public interface UnGroupTableGroupValidator {
    void validate(List<Long> orderTableIds);
}
