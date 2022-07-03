package kitchenpos.table_group.application;

import java.util.List;

public interface TableGroupValidator {

    void checkValidUngroup(List<Long> orderTableIds);

}
