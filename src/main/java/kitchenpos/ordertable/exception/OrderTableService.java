package kitchenpos.ordertable.exception;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.ChangeEmptyOrderTableValidator;
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
    private static final String NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE = "주문 테이블이 존재하지 않습니다.";
    private static final String CHANGE_EMPTY_NOT_FOUND_ERROR_MESSAGE = "존재하는 주문 테이블만 빈 테이블 유무를 변경할 수 있습니다.";
    private static final String CHANGE_NUMBER_OF_GUEST_NOT_FOUND_ERROR_MESSAGE = "존재하는 주문 테이블만 방문자 수를 변경 할 수 있습니다.";
    private static final String ILLEGAL_IDS_ERROR_MESSAGE = "올바르지 않는 아이디 목록 입니다.";

    private final ChangeEmptyOrderTableValidator changeEmptyOrderTableValidator;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final ChangeEmptyOrderTableValidator changeEmptyOrderTableValidator,
                             final OrderTableRepository orderTableRepository) {
        this.changeEmptyOrderTableValidator = changeEmptyOrderTableValidator;
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
                .orElseThrow(() -> {
                    throw new CanNotChangeOrderTableException(CHANGE_EMPTY_NOT_FOUND_ERROR_MESSAGE);
                });
        changeEmptyOrderTableValidator.validate(orderTableId);
        orderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long id, final OrderTableRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> {
                    throw new CanNotChangeOrderTableException(CHANGE_NUMBER_OF_GUEST_NOT_FOUND_ERROR_MESSAGE);
                });
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<OrderTable> getOrderTablesByIdIn(List<Long> orderTableIds) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalOrderTableIdsException(ILLEGAL_IDS_ERROR_MESSAGE);
        }
        return savedOrderTables;
    }

    @Transactional(readOnly = true)
    public OrderTable getOrderTable(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundOrderTableException(NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE);
                });
    }
}
