package kitchenpos.table.ui;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<TableResponse> create(@RequestBody final TableRequest request) {
        final OrderTable created = tableService.create(request.toEntity());
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(new TableResponse(created))
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok()
                .body(tableService.list().stream()
                        .map(TableResponse::new)
                        .collect(toList()))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(
            @PathVariable final Long orderTableId,
            @RequestBody final TableEmptyRequest request
    ) {
        OrderTable orderTable = tableService.changeEmpty(orderTableId, request.isEmpty());
        return ResponseEntity.ok()
                .body(new TableResponse(orderTable))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final TableNumberOfGuestsRequest request
    ) {
        OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests());
        return ResponseEntity.ok()
                .body(new TableResponse(orderTable))
                ;
    }

    static class TableEmptyRequest {
        private boolean empty;

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }
    }

    static class TableNumberOfGuestsRequest {
        private int numberOfGuests;

        public int getNumberOfGuests() {
            return numberOfGuests;
        }

        public void setNumberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
        }
    }

    static class TableRequest {
        private int numberOfGuests;
        private boolean empty;

        public int getNumberOfGuests() {
            return numberOfGuests;
        }

        public void setNumberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public OrderTable toEntity() {
            return new OrderTable(numberOfGuests, empty);
        }
    }

    static class TableResponse {
        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        public TableResponse(OrderTable orderTable) {
            id = orderTable.getId();
            if (orderTable.getTableGroup() != null) {
                tableGroupId = orderTable.getTableGroup().getId();
            }
            numberOfGuests = orderTable.getNumberOfGuests();
            empty = orderTable.isEmpty();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getTableGroupId() {
            return tableGroupId;
        }

        public void setTableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
        }

        public int getNumberOfGuests() {
            return numberOfGuests;
        }

        public void setNumberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }
    }
}
