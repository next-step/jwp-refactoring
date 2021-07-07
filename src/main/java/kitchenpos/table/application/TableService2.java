package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableService2 {
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    public TableService2(final OrderRepository orderRepository, final TableRepository tableRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
    }

    public TableResponse create(final TableRequest request) {
        return TableResponse.from(tableRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<TableResponse> findAllTables() {
        return TableResponse.ofList(tableRepository.findAll());
    }

    public TableResponse changeEmpty(final Long orderTableId, final TableRequest request) {
        OrderTableEntity table = findTableById(orderTableId);
        validateTableInUse(orderTableId);
        table.changeEmpty(request.isEmpty());
        return TableResponse.from(table);
    }

    private OrderTableEntity findTableById(Long orderTableId) {
        return tableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private void validateTableInUse(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, OrderStatus.getBusyStatus())) {
            throw new IllegalArgumentException();
        }
    }

    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest request) {
        OrderTableEntity table = findTableById(orderTableId);
        table.changeNumberOfGuests(request.getNumberOfGuests());
        return TableResponse.from(table);
    }
}
