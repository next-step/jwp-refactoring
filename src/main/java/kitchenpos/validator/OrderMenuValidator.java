package kitchenpos.validator;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.exception.InvalidMenuInOrderLineItems;
import org.springframework.stereotype.Component;

@Component
public class OrderMenuValidator {

    private final MenuRepository menuRepository;

    public OrderMenuValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateOrderLineItems(List<Long> menuIds) {
        long countOfMatchMenuIds = menuRepository.countByIdIn(menuIds);
        if (menuIds.size() != countOfMatchMenuIds) {
            throw new InvalidMenuInOrderLineItems();
        }
    }
}
