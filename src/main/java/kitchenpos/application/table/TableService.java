package kitchenpos.application.table;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(final TableValidator tableValidator, final OrderTableRepository orderTableRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.from(
                orderTableRepository.save(orderTableRequest.toOrderTable())
        );
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.fromList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = this.findOrderTable(orderTableId);
        tableValidator.changeEmpty(orderTable);

        orderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        int numberOfGuests = orderTableRequest.getNumberOfGuests();
        OrderTable orderTable = this.findOrderTable(orderTableId);
        validateChangeNumberOfGuests(orderTable);

        orderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTable);
    }

    public OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
    }

    private void validateChangeNumberOfGuests(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
