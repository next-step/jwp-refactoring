package kitchenpos.table.application;

import java.util.List;

public interface OrderSupport {
    boolean validateOrderChangeable(Long orderTableId);

    boolean validateOrderChangeable(List<Long> orderTableIds);
}
