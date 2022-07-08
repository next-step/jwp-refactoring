package kitchenpos.order.domain;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final OrderTableDao orderTableDao;
    private final MenuDao menuDao;

    public OrderValidator(OrderTableDao orderTableDao, MenuDao menuDao) {
        this.orderTableDao = orderTableDao;
        this.menuDao = menuDao;
    }

    public void validate(OrderRequest orderRequest) {
        validateOrderTable(orderRequest.getOrderTableId());
        validateIfThereIsMenu(orderRequest.toMenuIds());
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(EntityNotFoundException::new);

        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIfThereIsMenu(List<Long> menuIds) {
        if(menuIds.size() != menuDao.countByIdIn(menuIds)) {
            throw new EntityNotFoundException();
        }
    }
}
