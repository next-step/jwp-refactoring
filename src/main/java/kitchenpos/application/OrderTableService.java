package kitchenpos.application;

import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.orderTable.SafeOrder;
import kitchenpos.domain.orderTable.SafeTableGroup;
import kitchenpos.domain.orderTable.exceptions.OrderTableEntityNotFoundException;
import kitchenpos.ui.dto.orderTable.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderTableService {
    private final SafeOrder safeOrder;
    private final SafeTableGroup safeTableGroup;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final SafeOrder safeOrder, final SafeTableGroup safeTableGroup,
                             final OrderTableRepository orderTableRepository) {
        this.safeOrder = safeOrder;
        this.safeTableGroup = safeTableGroup;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        OrderTable saved = orderTableRepository.save(orderTable);

        return OrderTableResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public OrderTable findOrderTable(final Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new OrderTableEntityNotFoundException("해당 주문 테이블이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest changeEmptyRequest) {
        final OrderTable foundOrderTable = this.findOrderTable(orderTableId);

        validateCanChangeEmpty(orderTableId);

        foundOrderTable.changeEmpty(changeEmptyRequest.isEmpty());

        return OrderTableResponse.of(foundOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId, final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest
    ) {
        final int numberOfGuests = changeNumberOfGuestsRequest.getNumberOfGuests();

        final OrderTable foundOrderTable = this.findOrderTable(orderTableId);

        foundOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(foundOrderTable);
    }

    private void validateCanChangeEmpty(final Long orderTableId) {
        safeTableGroup.canChangeEmptyStatus(orderTableId);
        safeOrder.canChangeEmptyStatus(orderTableId);
    }
}
