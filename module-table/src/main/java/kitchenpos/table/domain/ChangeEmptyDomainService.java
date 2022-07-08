package kitchenpos.table.domain;

import kitchenpos.table.exception.CannotChangeEmptyState;
import kitchenpos.table.exception.NotExistTableException;
import org.springframework.stereotype.Component;

@Component
public class ChangeEmptyDomainService {
    private final TableOrderStatusChecker tableOrderStatusChecker;
    private final OrderTableRepository orderTableRepository;

    public ChangeEmptyDomainService(TableOrderStatusChecker tableOrderStatusChecker,
                                    OrderTableRepository orderTableRepository) {
        this.tableOrderStatusChecker = tableOrderStatusChecker;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable changeEmpty(Long orderTableId, boolean isEmpty) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotExistTableException::new);
        if (tableOrderStatusChecker.isBeforeBillingStatus(orderTableId)) {
            throw new CannotChangeEmptyState(CannotChangeEmptyState.NOT_COMPLETED_ORDER);
        }
        savedOrderTable.changeEmpty(isEmpty);
        return savedOrderTable;
    }

}
