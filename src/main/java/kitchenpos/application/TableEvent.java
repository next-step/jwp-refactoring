package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;

public class TableEvent {

    protected TableEvent() {
    }

    public static class Grouped {
        private TableGroup tableGroup;
        private List<OrderTable> orderTables;

        private Grouped(final TableGroup tableGroup, final List<OrderTable> orderTables) {
            this.tableGroup = tableGroup;
            this.orderTables = orderTables;
        }

        public static Grouped from(final TableGroup tableGroup, final List<OrderTable> orderTables) {
            return new Grouped(tableGroup, orderTables);
        }

        public TableGroup getTableGroup() {
            return tableGroup;
        }

        public List<OrderTable> getOrderTables() {
            return orderTables;
        }
    }

    public static class Ungrouped {
        private Long tableGroupId;

        public Ungrouped(final Long tableGroupId) {
            this.tableGroupId = tableGroupId;
        }

        public static Ungrouped from(final Long tableGroupId) {
            return new Ungrouped(tableGroupId);
        }

        public Long getTableGroupId() {
            return tableGroupId;
        }
    }
}
