package kitchenpos.table.application;

import java.util.List;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.ui.request.OrderTableRequest;
import kitchenpos.order.ui.request.TableGuestsCountRequest;
import kitchenpos.order.ui.request.TableStatusRequest;
import kitchenpos.order.ui.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableRequest request) {
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
        return OrderTableResponse.from(orderTable);
    }

    public OrderTable findById(long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException(String.format("주문 테이블 id(%d)를 찾을 수 없습니다.", id)));
    }
}
