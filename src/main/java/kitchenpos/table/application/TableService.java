package kitchenpos.table.application;

import kitchenpos.table.domain.TableDomainService;
import kitchenpos.table.dto.EmptyRequest;
import kitchenpos.table.dto.NumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableDomainService tableDomainService;

    public TableService(OrderTableRepository orderTableRepository,
                        TableDomainService tableDomainService) {
        this.orderTableRepository = orderTableRepository;
        this.tableDomainService = tableDomainService;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(request.toOrderTable()));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.of(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, EmptyRequest request) {
        OrderTable orderTable = tableDomainService.findById(orderTableId);
        orderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, NumberOfGuestsRequest request) {
        OrderTable orderTable = tableDomainService.findById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    public List<OrderTable> findAllIdIn(List<Long> ids) {
        return tableDomainService.findAllIdIn(ids);
    }
}
