package kitchenpos.domain.tablegroup;

import java.util.List;

public interface SafeOrderInTableGroup {
    void canUngroup(List<Long> orderTableIds);
}
