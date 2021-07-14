package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.NonEmptyOrderTableNotFoundException;
import kitchenpos.table.exception.OrderTableNotFoundException;

@Service
@Transactional
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableRepository.save(orderTableRequest.toOrderTable()));
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id).orElseThrow(() -> new OrderTableNotFoundException("대상 주문테이블이 존재하지 않습니다. ID : " + id));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = findOrderTableByIdAndEmptyIsFalse(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        orderValidator.validateExistsOrderStatusIsCookingANdMeal(orderTableId);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableNotFoundException("대상 주문테이블이 존재하지 않습니다. 입력 ID : " + orderTableId));
    }

    public OrderTable findOrderTableByIdAndEmptyIsFalse(Long orderTableId) {
        return orderTableRepository.findByIdAndEmptyIsFalse(orderTableId)
                .orElseThrow(() -> new NonEmptyOrderTableNotFoundException("비어있지 않은 테이블 대상이 존재하지 않습니다. 입력 ID : " + orderTableId));
    }
}
