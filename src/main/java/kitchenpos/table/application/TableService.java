package kitchenpos.table.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableOrderStatusChecker;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.CanNotMakeOrderTableException;
import kitchenpos.table.exception.CannotChangeEmptyState;
import kitchenpos.table.exception.NotExistTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final TableOrderStatusChecker tableOrderStatusChecker;
    private final OrderTableRepository orderTableRepository;

    public TableService(TableOrderStatusChecker tableOrderStatusChecker,
                        OrderTableRepository orderTableRepository) {
        this.tableOrderStatusChecker = tableOrderStatusChecker;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(toList());
    }

    public List<OrderTableResponse> findAllByTableGroupId(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        if (tableOrderStatusChecker.isBeforeBillingStatus(orderTableId)) {
            throw new CannotChangeEmptyState("not completed order exist");
        }
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    public void validateTableToMakeOrder(Long orderTableId) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        if(orderTable.isEmpty()){
            throw new CanNotMakeOrderTableException(String.valueOf(orderTable.getId()));
        }
    }

    private OrderTable findOrderTableById(Long orderTableId){
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NotExistTableException::new);
    }
}
