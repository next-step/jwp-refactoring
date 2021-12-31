package kitchenpos.ordertable;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.CanNotChangeOrderTableException;
import kitchenpos.ordertable.validator.OrderTableChangeEmptyValidator;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderTableService {
    private static final String CHANGE_EMPTY_NOT_FOUND_ERROR_MESSAGE = "존재하는 주문 테이블만 빈 테이블 유무를 변경할 수 있습니다.";
    private static final String CHANGE_NUMBER_OF_GUEST_NOT_FOUND_ERROR_MESSAGE = "존재하는 주문 테이블만 방문자 수를 변경 할 수 있습니다.";

    private final OrderTableChangeEmptyValidator changeEmptyOrderTableValidator;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final OrderTableChangeEmptyValidator changeEmptyOrderTableValidator,
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
        orderTable.changeEmpty(request.isEmpty(), changeEmptyOrderTableValidator);
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
}
