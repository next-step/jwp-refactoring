package kitchenpos.table.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupId;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.table.exception.NotFoundOrderTableException;
import kitchenpos.order.domain.OrderTableId;
import kitchenpos.table.domain.TableValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(
        final TableValidator tableValidator,
        final OrderTableRepository orderTableRepository
    ) {
        this.tableValidator = tableValidator;
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
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                            .orElseThrow(NotFoundOrderTableException::new);

        orderTable.changeEmpty(orderTableDto.isEmpty(), tableValidator);

        return OrderTableDto.of(orderTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                            .orElseThrow(NotFoundOrderTableException::new);

        orderTable.changeNumberOfGuests(orderTableDto.getNumberOfGuests(), tableValidator);

        return OrderTableDto.of(orderTable);
    }

    public OrderTable findById(OrderTableId orderTableId) {
        return orderTableRepository.findById(orderTableId.value()).orElseThrow(NotFoundOrderTableException::new);
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> findByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(TableGroupId.of(tableGroupId));
    }
}
