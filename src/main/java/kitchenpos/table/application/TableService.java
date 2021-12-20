package kitchenpos.table.application;

import java.util.List;
import kitchenpos.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }


    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {

        OrderTable orderTable = orderTableRepository.save(orderTableRequest.toEntity());

        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.ofList(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final Boolean emptyTable) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.setEmpty(emptyTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final Integer guestNumber) {

        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.setNumberOfGuests(guestNumber);

        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable findOrderTable(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당하는 테이블을 찾을 수 없습니다."));
    }
}
