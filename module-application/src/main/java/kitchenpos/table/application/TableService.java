package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.NoOrderTableException;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@Service
public class TableService {
    private OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTable) {
        OrderTable newTable = new OrderTable(orderTable.getNumberOfGuests(), orderTable.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(newTable));
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> list = orderTableRepository.findAll();
        List<OrderTableResponse> listResponse = list.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());

        return listResponse;
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(NoOrderTableException::new);

        savedOrderTable.updateEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(NoOrderTableException::new);

        savedOrderTable.updateGuests(orderTableRequest.getNumberOfGuests());

        return OrderTableResponse.of(savedOrderTable);
    }
}
