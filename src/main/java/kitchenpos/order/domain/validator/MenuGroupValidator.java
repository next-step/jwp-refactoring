package kitchenpos.order.domain.validator;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MenuGroupValidator {
    void validate(List<Long> menuIds);
}
