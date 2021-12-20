package kichenpos.table.table.application;

import java.util.List;
import kichenpos.table.table.domain.TableCommandService;
import kichenpos.table.table.domain.TableQueryService;
import kichenpos.table.table.ui.request.EmptyRequest;
import kichenpos.table.table.ui.request.OrderTableRequest;
import kichenpos.table.table.ui.request.TableGuestsCountRequest;
import kichenpos.table.table.ui.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final TableCommandService commandService;
    private final TableQueryService queryService;

    public TableService(TableCommandService commandService,
        TableQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.from(commandService.save(request.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listFrom(queryService.findAll());
    }

    public OrderTableResponse findById(long id) {
        return OrderTableResponse.from(queryService.table(id));
    }

    @Transactional
    public OrderTableResponse changeEmpty(long id, EmptyRequest request) {
        return OrderTableResponse.from(commandService.changeEmpty(id, request.isEmpty()));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(long id, TableGuestsCountRequest request) {
        return OrderTableResponse.from(
            commandService.changeNumberOfGuests(id, request.numberOfGuests()));
    }

    @Transactional
    public void changeOrdered(long orderTableId) {
        commandService.changeOrdered(orderTableId);
    }

    @Transactional
    public void changeFinish(long orderTableId) {
        commandService.changeFinish(orderTableId);
    }
}
