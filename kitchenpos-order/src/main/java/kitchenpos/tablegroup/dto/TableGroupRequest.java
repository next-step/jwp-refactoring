package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<GroupTableRequest> orderTables;

    public TableGroupRequest() {}

    public TableGroupRequest(List<GroupTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables.stream()
                .map(GroupTableRequest::getId)
                .collect(Collectors.toList());
    }

    public static class GroupTableRequest {
        private Long id;

        public GroupTableRequest() {
        }

        public GroupTableRequest(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
