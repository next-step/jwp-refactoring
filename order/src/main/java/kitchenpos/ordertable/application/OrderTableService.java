package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.domain.Validator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final Validator<OrderTable> validator;

    public OrderTableService(
        final OrderTableRepository orderTableRepository,
        final Validator<OrderTable> validator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.validator = validator;
    }

    @Transactional
    public OrderTableResponse create() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable());
        return OrderTableResponse.of(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(
        final Long orderTableId,
        final OrderTableChangeEmptyRequest request
    ) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 테이블을 변경할 수 없습니다."));
        if (request.isEmpty()) {
            orderTable.clear(validator);

        }
        if (!request.isEmpty()) {
            orderTable.serve();
        }
        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
        final Long orderTableId,
        final OrderTableChangeNumberOfGuestsRequest request
    ) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 테이블을 변경할 수 없습니다."));
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }
}
