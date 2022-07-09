package kitchenpos.table.application;

import java.util.List;

public interface TableGroupValidator {

    void validateUngroup(List<Long> orderTableIds);

}
