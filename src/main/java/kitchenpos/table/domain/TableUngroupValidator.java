package kitchenpos.table.domain;

import java.util.List;

public interface TableUngroupValidator {
    void validate(final List<Long> orderTableIds);
}
