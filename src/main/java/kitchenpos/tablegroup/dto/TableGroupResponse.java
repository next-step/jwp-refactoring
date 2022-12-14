package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tablegroup.domain.GroupTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<GroupTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<GroupTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup savedTableGroup, List<GroupTable> tables) {
        List<GroupTableResponse> groupTables = tables.stream()
                .map(GroupTableResponse::from)
                .collect(Collectors.toList());
        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(), groupTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<GroupTableResponse> getOrderTables() {
        return orderTables;
    }

    public static class GroupTableResponse {
        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        public GroupTableResponse() {
        }

        public GroupTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
            this.id = id;
            this.tableGroupId = tableGroupId;
            this.numberOfGuests = numberOfGuests;
            this.empty = empty;
        }

        public static GroupTableResponse from(GroupTable table) {
            return new GroupTableResponse(table.getId(), table.getTableGroupId(), table.getNumberOfGuests(), table.isEmpty());
        }

        public Long getId() {
            return id;
        }

        public Long getTableGroupId() {
            return tableGroupId;
        }

        public int getNumberOfGuests() {
            return numberOfGuests;
        }

        public boolean isEmpty() {
            return empty;
        }
    }
}
