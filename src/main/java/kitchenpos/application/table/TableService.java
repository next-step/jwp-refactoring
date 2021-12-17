package kitchenpos.application.table;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.order.OrderService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.exception.table.NotFoundOrderTableException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;

    public TableService(
        final OrderService orderService,
        final OrderTableRepository orderTableRepository
    ) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableDto create(final OrderTableDto orderTable) {
        return OrderTableDto.of(orderTableRepository.save(OrderTable.of(orderTable.getNumberOfGuests(), orderTable.getEmpty())));
    }

    @Transactional(readOnly = true)
    public List<OrderTableDto> list() {
        return orderTableRepository.findAll().stream()
                                    .map(OrderTableDto::of)
                                    .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                                .orElseThrow(NotFoundOrderTableException::new);

        savedOrderTable.changeEmpty(orderTable.isEmpty(), orderService.findByOrderTableId(orderTableId));

        return OrderTableDto.of(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableDto orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                                .orElseThrow(NotFoundOrderTableException::new);

        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

        return orderTableRepository.save(savedOrderTable);
    }
}
