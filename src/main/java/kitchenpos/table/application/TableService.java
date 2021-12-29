package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = request.toOrderTable();

        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, boolean isEmpty) {
        final OrderTable orderTable = findById(orderTableId);
        
        orderTable.changeEmpty(isEmpty);
        
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable orderTable = findById(orderTableId);
        
        orderTable.changeNumberOfGuests(numberOfGuests);
        
        return OrderTableResponse.from(orderTable);
    }
    
    @Transactional(readOnly = true)
    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 주문 테이블이 없습니다"));
    }
    
    @Transactional(readOnly = true)
    public List<OrderTable> findByOrderTables(List<OrderTable> request) {
        return request.stream()
                .map(orderTable -> findById(orderTable.getId()))
                .collect(Collectors.toList());
    }
}
