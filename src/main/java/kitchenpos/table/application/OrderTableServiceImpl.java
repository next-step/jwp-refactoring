package kitchenpos.table.application;

import kitchenpos.order.application.OrderTableService;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderTableServiceImpl implements OrderTableService {

    private OrderTableDao orderTableDao;

    public OrderTableServiceImpl(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public boolean isTableEmpty(Long tableId) {
        OrderTable orderTable = getOrderTable(tableId);
        return orderTable.isEmpty();
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
