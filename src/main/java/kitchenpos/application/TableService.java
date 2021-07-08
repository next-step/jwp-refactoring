package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.exception.OrderTableNotFoundException;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest request) {
        return orderTableRepository.save(request.toEntity());
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTable.numberOfGuests());

        return savedOrderTable;
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new OrderTableNotFoundException("해당 ID 의 주문 테이블이 존재하지 않습니다."));
    }
}
