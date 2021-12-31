package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.validator.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableValidator orderTableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(
            final OrderTableValidator orderTableValidator,
            final OrderTableRepository orderTableRepository) {
        this.orderTableValidator = orderTableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableRepository.save(
                orderTableRequest.toOrderTable()
        ));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest changeEmptyRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findByIdElseThrow(orderTableId);

        orderTableValidator.checkTableOrderStatus(savedOrderTable);
        savedOrderTable.changeEmpty(changeEmptyRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeGuestsRequest changeGuestsRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findByIdElseThrow(orderTableId);
        savedOrderTable.changeNumberOfGuests(changeGuestsRequest.toNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    public boolean isExistsByIdAndEmptyTrue(final Long id) {
        return orderTableRepository.existsByIdAndEmptyTrue(id);
    }

    public boolean isExistsById(Long id) {
        return orderTableRepository.existsById(id);
    }
}
