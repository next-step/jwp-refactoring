package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableStatus;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.table.application.TableGroupService.ALREADY_USE_ORDER_TABLE;

@Service
@Transactional
public class TableService {
    private static final String INVALID_NUMBER_OF_GUESTS = "방문 고객 수는 0 이상이어야 합니다.";

    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableRepository.save(new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.getTableStatus())));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.validateTableGroupIsNull();
        validateOrderTableAlreadyUse(orderTableId);
        savedOrderTable.changeTableStatus(orderTableRequest.getTableStatus());

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();

        validateNumberOfGuests(numberOfGuests);

        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.validateNotEmpty();
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(INVALID_NUMBER_OF_GUESTS);
        }
    }

    private void validateOrderTableAlreadyUse(Long orderTableId) {
        if (orderTableRepository.existsByIdAndTableStatus(orderTableId, TableStatus.IN_USE)) {
            throw new IllegalArgumentException(ALREADY_USE_ORDER_TABLE);
        }
    }
}
