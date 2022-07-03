package kitchenpos.table.application;

import kitchenpos.Exception.NotFoundOrderTableException;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return OrderTableResponse.from(orderTableRepository.save(orderTableRequest.toOrderTable()));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);

        tableValidator.validate(orderTableId);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);

        savedOrderTable.changeNumberOfGuests(NumberOfGuests.from(orderTableRequest.getNumberOfGuests()));

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

}
