package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.ui.request.OrderTableRequest;
import kitchenpos.table.ui.request.TableGuestsCountRequest;
import kitchenpos.table.ui.request.TableStatusRequest;
import kitchenpos.table.ui.response.OrderTableResponse;
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
        OrderTable table = orderTableRepository.table(id);
        table.changeEmpty(request.isEmpty());
        return OrderTableResponse.from(table);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(long id, TableGuestsCountRequest request) {
        OrderTable orderTable = orderTableRepository.table(id);
        orderTable.changeNumberOfGuests(request.numberOfGuests());
        return OrderTableResponse.from(orderTable);
    }
}
