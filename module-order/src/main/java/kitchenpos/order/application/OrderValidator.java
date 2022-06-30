package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public long menuCountByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }
}
