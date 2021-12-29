package kitchenpos.table.event;

import java.util.List;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableEvent {

	private TableEvent() {
	}

	public static class Grouped {
		private TableGroup tableGroup;
		private List<OrderTable> orderTables;

		private Grouped(TableGroup tableGroup, List<OrderTable> orderTables) {
			this.tableGroup = tableGroup;
			this.orderTables = orderTables;
		}

		public static Grouped from(TableGroup tableGroup, List<OrderTable> orderTables) {
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

		public Ungrouped(Long tableGroupId) {
			this.tableGroupId = tableGroupId;
		}

		public static Ungrouped from(Long tableGroupId) {
			return new Ungrouped(tableGroupId);
		}

		public Long getTableGroupId() {
			return tableGroupId;
		}
	}

}
