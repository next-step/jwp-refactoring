package kitchenpos.ui;

import kitchenpos.application.command.TableQueryService;
import kitchenpos.application.command.TableService;
import kitchenpos.domain.NumberOfGuest;
import kitchenpos.dto.request.ChangeEmptyRequest;
import kitchenpos.dto.request.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;
    private final TableQueryService tableQueryService;

    public TableRestController(TableService tableService, TableQueryService tableQueryService) {
        this.tableService = tableService;
        this.tableQueryService = tableQueryService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableViewResponse> create(@RequestBody final OrderTableCreateRequest orderTableCreateRequest) {
        final Long id = tableService.create(orderTableCreateRequest.toCreate());

        return ResponseEntity.created(URI.create("/api/tables/" + id))
                .body(tableQueryService.findById(id));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableViewResponse>> list() {
        return ResponseEntity.ok()
                .body(tableQueryService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableViewResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeEmptyRequest changeEmptyRequest
    ) {
        tableService.changeEmpty(orderTableId, changeEmptyRequest.isEmpty());

        return ResponseEntity.ok()
                .body(tableQueryService.findById(orderTableId));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableViewResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest
    ) {
        NumberOfGuest numberOfGuest = new NumberOfGuest(changeNumberOfGuestsRequest.getNumberOfGuests());

        tableService.changeNumberOfGuests(orderTableId, numberOfGuest);

        return ResponseEntity.ok()
                .body(tableQueryService.findById(orderTableId));
    }
}
