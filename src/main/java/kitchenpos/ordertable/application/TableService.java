package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.validator.OrderTableValidators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidators orderTableValidators;

    public TableService(OrderTableRepository orderTableRepository,
                        OrderTableValidators orderTableValidators) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidators = orderTableValidators;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest emptyRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 주문 테이블의 비어있음 여부를 수정할 수 없습니다[orderTableId:" + orderTableId + "]"));

        orderTable.changeEmpty(emptyRequest.isEmpty());
        orderTableValidators.validateChangeEmpty(orderTable);
        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeNumberOfGuestsRequest numberOfGuestsRequest) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 주문 테이블의 방문한 손님수를 수정할 수 없습니다[orderTableId:" + orderTableId + "]"));

        savedOrderTable.changeNumberOfGuests(numberOfGuestsRequest.getNumberOfGuests());

        return savedOrderTable;
    }
}
