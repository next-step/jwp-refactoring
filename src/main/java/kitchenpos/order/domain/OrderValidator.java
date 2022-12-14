package kitchenpos.order.domain;

import java.util.List;

public interface OrderValidator {
    void checkOrderTableIsNotEmpty(Long orderTableId);

    void checkEmptyChangeable(Long orderTableId);

    void checkUnGroupable(List<Long> orderTableIds);
}
