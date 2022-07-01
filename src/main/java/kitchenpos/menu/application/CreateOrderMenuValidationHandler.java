package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.event.CreateOrderEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderMenuValidationHandler {

    private final MenuRepository menuRepository;

    public CreateOrderMenuValidationHandler(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void validateOrderTable(CreateOrderEvent event) {
        if (!menuRepository.existsAllByIdIn(event.getMenuIds())) {
            throw new IllegalArgumentException("등록되어 있지 않은 주문 항목이 존재 합니다.");
        }
    }
}
