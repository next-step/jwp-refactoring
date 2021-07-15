package kitchenpos.ordertable.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.generic.exception.OrderTableNotFoundException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTableValidator;
import kitchenpos.ordertable.dto.OrderTableRequest;

@Service
public class TableService {

    private final OrderTableValidator validator;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableValidator validator, OrderTableRepository orderTableRepository) {
        this.validator = validator;
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
        savedOrderTable.changeEmpty(validator, request.isEmpty());

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuests(validator, orderTable.numberOfGuests());

        return savedOrderTable;
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new OrderTableNotFoundException("해당 ID 의 주문 테이블이 존재하지 않습니다."));
    }
}
