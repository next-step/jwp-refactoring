package kitchenpos.menus.menu.application;

import kitchenpos.menus.menu.domain.MenuRepository;
import kitchenpos.menus.menu.dto.OrderMenuRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderMenuValidator {

    private final MenuRepository menuRepository;

    public OrderMenuValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(OrderMenuRequest request) {
        if (!menuRepository.existsAllByIdIn(request.getMenuIds())) {
            throw new IllegalArgumentException("등록되어 있지 않은 메뉴가 존재 합니다.");
        }
    }
}
