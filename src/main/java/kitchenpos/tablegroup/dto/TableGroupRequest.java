package kitchenpos.tablegroup.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<Ids> orderTables = new ArrayList<>();

    public TableGroupRequest() {}

    public TableGroupRequest(List<Long> ids) {
        orderTables = ids.stream().map(Ids::new).collect(Collectors.toList());
    }

    public List<Long> ids() {
        return orderTables.stream().map(Ids::getId).collect(Collectors.toList());
    }

    public List<Ids> getOrderTables() {
        return orderTables;
    }

    public static class Ids{
        private Long id;

        public Ids() {
        }

        public Ids(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}

