package kitchenpos.order.validator;

import java.util.List;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.validator.TableGroupOrderValidator;
import org.springframework.stereotype.Component;

@Component
public class TableGroupOrderValidatorImpl implements TableGroupOrderValidator {

    private final OrderDao orderDao;

    public TableGroupOrderValidatorImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public boolean existsDinningTable(List<Long> tableIds) {
        return orderDao.existsByOrderTableIdInAndOrderStatusNot(tableIds, OrderStatus.COMPLETION);
    }
}
