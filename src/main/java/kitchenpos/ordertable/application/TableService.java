package kitchenpos.ordertable.application;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.save(orderTableRequest.toEntity());
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable findOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));

        findOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(findOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTable) {
        final OrderTable findOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));
        findOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());
        return OrderTableResponse.of(findOrderTable);
    }
}
