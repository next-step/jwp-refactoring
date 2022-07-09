package kitchenpos.order.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import kitchenpos.common.exception.InvalidValueException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validate(OrderRequest orderRequest) {
        validateOrderTable(orderRequest.getOrderTableId());
        validateIfThereIsMenu(orderRequest.toMenuIds());
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(EntityNotFoundException::new);

        if(orderTable.isEmpty()) {
            throw new InvalidValueException();
        }
    }

    private void validateIfThereIsMenu(List<Long> menuIds) {
        if(menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new EntityNotFoundException();
        }
    }
}
