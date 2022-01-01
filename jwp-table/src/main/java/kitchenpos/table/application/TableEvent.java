package kitchenpos.table.application;

import java.util.List;

public class TableEvent {

    protected TableEvent() {
    }

    public static class Grouped {
        private Long tableGroupId;
        private List<Long> orderTableIds;

        private Grouped(final Long tableGroupId, final List<Long> orderTableIds) {
            this.tableGroupId = tableGroupId;
            this.orderTableIds = orderTableIds;
        }

        public static Grouped from(final Long tableGroupId, final List<Long> orderTableIds) {
            return new Grouped(tableGroupId, orderTableIds);
        }

        public Long getTableGroupId() {
            return tableGroupId;
        }

        public List<Long> getOrderTableIds() {
            return orderTableIds;
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
