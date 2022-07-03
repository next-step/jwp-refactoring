package kitchenpos.table.ui;

import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest request) {
        final TableGroup created = tableGroupService.create(request.getOrderTableIds());
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(new TableGroupResponse(created))
                ;
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }

    static class TableGroupRequest {
        List<TableGroupOrderTableRequest> orderTables;

        public List<TableGroupOrderTableRequest> getOrderTables() {
            return orderTables;
        }

        public void setOrderTables(List<TableGroupOrderTableRequest> orderTables) {
            this.orderTables = orderTables;
        }

        public List<Long> getOrderTableIds() {
            return orderTables.stream()
                    .map(TableGroupOrderTableRequest::getId)
                    .collect(toList());
        }
    }

    static class TableGroupOrderTableRequest {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    static class TableGroupResponse {
        private Long id;
        private LocalDateTime createdDate;
        private List<TableGroupOrderTableResponse> orderTables;

        public TableGroupResponse(TableGroup tableGroup) {
            id = tableGroup.getId();
            createdDate = tableGroup.getCreatedDate();
            orderTables = tableGroup.getGroupTables().getOrderTables().stream()
                    .map(TableGroupOrderTableResponse::new)
                    .collect(toList());
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public LocalDateTime getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
        }

        public List<TableGroupOrderTableResponse> getOrderTables() {
            return orderTables;
        }

        public void setOrderTables(List<TableGroupOrderTableResponse> orderTables) {
            this.orderTables = orderTables;
        }
    }

    static class TableGroupOrderTableResponse {
        private Long id;
        private int numberOfGuests;
        private boolean empty;

        public TableGroupOrderTableResponse(OrderTable orderTable) {
            id = orderTable.getId();
            numberOfGuests = orderTable.getNumberOfGuests();
            empty = orderTable.isEmpty();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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
