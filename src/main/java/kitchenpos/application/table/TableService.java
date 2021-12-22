package kitchenpos.application.table;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.exception.table.NotFoundOrderTableException;
import kitchenpos.vo.OrderTableId;
import kitchenpos.vo.TableGroupId;

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
        OrderTable validatedOrderTable = tableValidator.getValidatedOrderTableForChangeEmpty(orderTableId);

        validatedOrderTable.changeEmpty(orderTableDto.isEmpty());

        return OrderTableDto.of(validatedOrderTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable validatedOrderTable = tableValidator.getValidatedOrderTableForChangeNumberOfGuests(orderTableId, orderTableDto.getNumberOfGuests());

        validatedOrderTable.changeNumberOfGuests(orderTableDto.getNumberOfGuests());

        return OrderTableDto.of(validatedOrderTable);
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
