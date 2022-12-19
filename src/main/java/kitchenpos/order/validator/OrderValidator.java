package kitchenpos.order.validator;

import java.util.List;

public interface OrderValidator {

    void validateOrderCreate(Long orderTableId, List<Long> meneIds);
    void validateOrderComplete(List<Long> orderTableIds);
    void validateOrderComplete(Long orderTableId);
}
