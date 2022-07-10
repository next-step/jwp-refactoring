package kitchenpos.order.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator extends AbstractAggregateRoot<OrderValidator> {
    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateOrderTable(Long orderTableId) {
        this.registerEvent(orderTableId);
    }

    public void validateIfThereIsMenu(List<Long> menuIds) {
        if(menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new EntityNotFoundException();
        }
    }
}
