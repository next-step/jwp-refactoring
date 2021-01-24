package kitchenpos.order.application;

import kitchenpos.dao.table.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;

@Service
public class OrderTableService {
    private final OrderTableDao orderTableDao;
    public OrderTableService(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public OrderTable findById(Long id) {
        final OrderTable orderTable = orderTableDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return orderTable;
    }
}
