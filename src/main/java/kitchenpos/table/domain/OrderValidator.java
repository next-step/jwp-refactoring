package kitchenpos.table.domain;

import java.util.List;

public interface OrderValidator {
    void canUngroupOrChange(Long id);
    void canUngroupOrChangeOrderList(List<Long> orderTableIds);
}
