package kitchenpos.table.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStateValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderStateValidator changeOrderTableValidator;

    public TableService(final OrderStateValidator changeOrderTableValidator, final OrderTableRepository orderTableRepository) {
        this.changeOrderTableValidator = changeOrderTableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable saveTable = orderTableRepository.save(
                OrderTable.of(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty()));

        return OrderTableResponse.formOrderTable(saveTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::formOrderTable)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("변경할 테이블이 존재하지 않습니다."));

        changeOrderTableValidator.validateChangeEmptyTable(orderTableId);

        savedOrderTable.changeEmptyTable();

        return OrderTableResponse.formOrderTable(savedOrderTable);
    }


    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("오더 테이블이 존재하지 않습니다."));

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.formOrderTable(savedOrderTable);
    }
}
