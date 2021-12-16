package kitchenpos.table.application;

import java.util.List;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.ui.request.OrderTableRequest;
import kitchenpos.table.ui.request.TableGuestsCountRequest;
import kitchenpos.table.ui.request.TableStatusRequest;
import kitchenpos.table.ui.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.from(orderTableRepository.save(request.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listFrom(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(long id, TableStatusRequest request) {
        OrderTable orderTable = findById(id);
        orderTable.changeStatus(request.status());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
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
