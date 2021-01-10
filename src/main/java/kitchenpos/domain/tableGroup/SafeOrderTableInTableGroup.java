package kitchenpos.domain.tableGroup;

import java.util.List;

public interface SafeOrderTableInTableGroup {
    void canGroupTheseTables(List<Long> orderTableIds);
}
