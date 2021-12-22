package kitchenpos.ordertable.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.infra.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderTableService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final OrderService orderService, final OrderTableRepository orderTableRepository) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableRepository.save(orderTableRequest.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateChangeEmpty(orderTableId);
        orderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    private void validateChangeEmpty(Long orderTableId) {
        if (orderService.isCookingOrMealStateByOrderTableId(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블의 주문 상태가 조리나 식사일 경우에만 테이블의 빈 유무를 변경할 수 있습니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long id, final OrderTableRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("올바르지 않는 아이디 목록 입니다.");
        }
        return savedOrderTables;
    }

    @Transactional(readOnly = true)
    public OrderTable getOrderTable(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
