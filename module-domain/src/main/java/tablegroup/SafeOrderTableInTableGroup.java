package kitchenpos.domain.tablegroup;

import java.util.List;

public interface SafeOrderTableInTableGroup {
    void canGroupTheseTables(List<Long> orderTableIds);
}
