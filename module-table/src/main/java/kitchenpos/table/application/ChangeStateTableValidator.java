package kitchenpos.table.application;

import java.util.List;

public interface ChangeStateTableValidator {

    void validateChangeEmptyTable(Long orderTableId);
    void validateUnGroupTableChange(List<Long> orderTableIds);
}
