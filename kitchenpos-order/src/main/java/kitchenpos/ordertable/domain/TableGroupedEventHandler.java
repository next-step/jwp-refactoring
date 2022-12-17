package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.tablegroup.domain.TableGroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TableGroupedEventHandler {
    private static final int MIN_GROUPING_CRITERIA = 2;
    private static final String REQUIRED_TABLE = "테이블은 2개 이상 지정되어야 합니다.";
    private static final String NOT_EXIST_TABLE = "테이블이 존재하지 않습니다.";

    private final OrderTableRepository orderTableRepository;

    public TableGroupedEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(TableGroupedEvent event) {
        List<Long> tableIds = event.getTableIds();
        validateMinCriteria(tableIds);
        List<OrderTable> tables = orderTableRepository.findAllByIdIn(tableIds);
        validateExist(tables, tableIds);
        tables.forEach(table -> table.groupBy(event.getTableGroupId()));
    }

    private void validateMinCriteria(List<Long> tableIds) {
        if (tableIds.size() < MIN_GROUPING_CRITERIA) {
            throw new IllegalArgumentException(REQUIRED_TABLE);
        }
    }

    private void validateExist(List<OrderTable> tables, List<Long> tableIds) {
        if (tables.size() != tableIds.size()) {
            throw new IllegalArgumentException(NOT_EXIST_TABLE);
        }
    }
}
