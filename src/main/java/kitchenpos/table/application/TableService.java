package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableDependencyHelper;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.FailedChangeEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TableService {
    private final TableDependencyHelper tableDependencyHelper;
    private final OrderTableRepository orderTableRepository;

    public TableService(final TableDependencyHelper tableDependencyHelper, final OrderTableRepository orderTableRepository) {
        this.tableDependencyHelper = tableDependencyHelper;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable persistOrderTable = orderTableRepository.save(
                new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty()));
        return OrderTableResponse.from(persistOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findById(orderTableId);
        if (tableDependencyHelper.existsByOrderTableIdAndOrderStatusNotCompletion(orderTableId)) {
            throw new FailedChangeEmptyException("주문 상태가 완료일때만 빈 테이블 여부 변경 가능합니다.");
        }

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable findById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(EntityNotFoundException::new);
    }
}
