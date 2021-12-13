package kitchenpos.table.application;

import java.util.List;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.ui.request.OrderTableRequest;
import kitchenpos.table.ui.request.TableGuestsCountRequest;
import kitchenpos.table.ui.request.TableStatusRequest;
import kitchenpos.table.ui.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest request) {
        return OrderTableResponse.from(orderTableRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.listFrom(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(long id, TableStatusRequest request) {
        OrderTable orderTable = findById(id);
        orderTable.changeStatus(request.status());
        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(long id, TableGuestsCountRequest request) {
        OrderTable orderTable = findById(id);
        orderTable.changeNumberOfGuests(request.numberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllById(orderTableIds);
    }

    public OrderTable findById(long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException(String.format("주문 테이블 id(%d)가 존재하지 않습니다.", id)));
    }
}
