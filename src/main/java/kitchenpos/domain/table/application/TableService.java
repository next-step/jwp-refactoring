package kitchenpos.domain.table.application;

import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.table.domain.OrderTableRepository;
import kitchenpos.domain.table.dto.TableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(
            OrderTableRepository orderTableRepository,
            OrderTableValidator orderTableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTable create(final TableRequest request) {
        return orderTableRepository.save(request.toOrderTable());
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final TableRequest request) {
        orderTableValidator.validateCompleteTable(orderTableId);

        final OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeEmpty(request.isEmpty());
        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableRequest request) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTable;
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
