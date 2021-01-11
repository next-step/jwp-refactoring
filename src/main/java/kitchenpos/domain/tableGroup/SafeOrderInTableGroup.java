package kitchenpos.domain.tableGroup;

import java.util.List;

public interface SafeOrderInTableGroup {
    void canUngroup(List<Long> orderTableIds);
}
