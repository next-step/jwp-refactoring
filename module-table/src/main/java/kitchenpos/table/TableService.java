package kitchenpos.table;

import kitchenpos.domain.OrderStatus;
import kitchenpos.table.dto.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderTableMapper mapper = Mappers.getMapper(OrderTableMapper.class);

    private final TableOrderRepository tableOrderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(TableOrderRepository tableOrderRepository, OrderTableRepository orderTableRepository) {
        this.tableOrderRepository = tableOrderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final TableAddRequest orderTable) {
        return mapper.toResponse(orderTableRepository.save(orderTable.toOrderTable()));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final TableEmptyChangeRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (tableOrderRepository.existsByOrderTable_IdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.changeEmpty(request.isEmpty());
        return mapper.toResponse(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final TableNumberChangeRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return mapper.toResponse(orderTableRepository.save(savedOrderTable));
    }

    public OrderTable getOne(Long id) {
        return orderTableRepository.getOne(id);
    }

    public boolean existsByOrderTable_IdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> asList) {
        return tableOrderRepository.existsByOrderTable_IdInAndOrderStatusIn(orderTableIds, asList);
    }
}
