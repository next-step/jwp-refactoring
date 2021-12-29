package kitchenpos.table.application;

import kitchenpos.exception.NotExistEntityException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.save(orderTableRequest.toEntity());
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse empty(final Long orderTableId) {

        final OrderTable savedOrderTable = findById(orderTableId);

        savedOrderTable.empty();
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse full(final Long orderTableId) {

        final OrderTable savedOrderTable = findById(orderTableId);

        savedOrderTable.full();
        return OrderTableResponse.of(savedOrderTable);
    }


    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {

        final OrderTable savedOrderTable = findById(orderTableId);

        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotExistEntityException("주문 테이블을 찾을 수 없습니다. orderTableId = " + orderTableId));
    }

    public List<OrderTable> findAllByIds(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new NotExistEntityException("일부 주문 테이블을 찾을 수 없습니다.");
        }

        return savedOrderTables;
    }

    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findByTableGroupId(tableGroupId);
    }
}
