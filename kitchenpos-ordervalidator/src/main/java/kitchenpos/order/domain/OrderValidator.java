package kitchenpos.order.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderValidator {

    void validateOrderStatusIsCookingOrMealByTableId(Long orderTableId);

    void validateOrderStatusIsCookingOrMealByTableIds(List<Long> orderTableIds);
}
