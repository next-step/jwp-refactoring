package kitchenpos.table.application;


import static kitchenpos.common.exception.ErrorCode.NOT_EXISTS_TABLE;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.table.application.validator.TableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final TableValidator tableValidator
    ) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTable orderTable) {
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderTable findById(Long orderTableId){
        return orderTableRepository.findById(orderTableId).orElseThrow(() -> new KitchenposException(NOT_EXISTS_TABLE));
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = findById(orderTableId);
        tableValidator.validateChangeEmpty(savedOrderTable);

        savedOrderTable.changeEmpty(empty);

        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        tableValidator.validateNumberOfGuests(orderTable.getNumberOfGuests());

        final int numberOfGuests = orderTable.getNumberOfGuests();

        final OrderTable savedOrderTable = findById(orderTableId);
        tableValidator.validateEmptyTrue(savedOrderTable);

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }
}
