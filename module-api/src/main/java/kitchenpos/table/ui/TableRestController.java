package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.moduledomain.table.OrderTable;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.TableGroupRequest;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeNumberOfGuestRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestController {

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(
        @RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTableResponse created = tableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created)
            ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok()
            .body(tableService.list())
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(
        @PathVariable final Long orderTableId,
        @RequestBody final ChangeEmptyRequest changeEmptyRequest
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeEmpty(orderTableId,
                changeEmptyRequest))
            ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
        @PathVariable final Long orderTableId,
        @RequestBody final ChangeNumberOfGuestRequest changeNumberOfGuestRequest
    ) {
        return ResponseEntity.ok()
            .body(tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestRequest))
            ;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<List<OrderTableResponse>> group(
        @RequestBody final TableGroupRequest tableGroupRequest) {
        final List<OrderTableResponse> grouped = tableService.group(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + grouped.get(0).getId());
        return ResponseEntity.created(uri)
            .body(grouped)
            ;
    }

    @DeleteMapping(value = "/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}
