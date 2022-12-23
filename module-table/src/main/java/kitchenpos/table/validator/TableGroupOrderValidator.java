package kitchenpos.table.validator;

import java.util.List;

public interface TableGroupOrderValidator {

    boolean existsDinningTable(List<Long> tableIds);
}
