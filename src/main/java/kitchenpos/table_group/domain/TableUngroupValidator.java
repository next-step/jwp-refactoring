package kitchenpos.table_group.domain;

import java.util.List;

public interface TableUngroupValidator {
    void validate(final List<Long> orderTableIds);
}
