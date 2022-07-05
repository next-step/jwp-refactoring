package kitchenpos.menu.domain;

import kitchenpos.order.domain.MenuCountValidateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ValidateMenuCountValidateEventHandler {
    private final MenuRepository menuRepository;

    public ValidateMenuCountValidateEventHandler(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(MenuCountValidateEvent event) {
        if (event.getMenuCount() != getMenuCount(event.getMenuIds())) {
            throw new IllegalArgumentException();
        }
    }

    private long getMenuCount(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }
}
