package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.NonEmptyOrderTableNotFoundException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.TableGroupAlreadyExistsException;

@Service
@Transactional
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderService orderService;

    public TableService(final OrderTableRepository orderTableRepository, final OrderService orderService) {
        this.orderTableRepository = orderTableRepository;
        this.orderService = orderService;
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
        OrderTable savedOrderTable = orderTableRepository.findByIdAndEmptyIsFalse(orderTableId)
                .orElseThrow(() -> new NonEmptyOrderTableNotFoundException("비어있지 않은 테이블 대상이 존재하지 않습니다. 입력 ID : " + orderTableId));
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTable> findOrderTablesByIds(List<Long> ids) {
        return orderTableRepository.findByIdIn(ids);
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        orderService.validateExistsOrderStatusIsCookingANdMeal(orderTableId);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableNotFoundException("대상 주문테이블이 존재하지 않습니다. 입력 ID : " + orderTableId));
        validateHasTabledGroup(savedOrderTable);
        return savedOrderTable;
    }

    private void validateHasTabledGroup(OrderTable savedOrderTable) {
        if (savedOrderTable.hasTableGroup()) {
            throw new TableGroupAlreadyExistsException();
        }
    }
}
