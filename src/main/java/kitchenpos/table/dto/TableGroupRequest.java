package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TableGroupRequest {
	private LocalDateTime createdDate;
	@NotNull
	@Size(min = 2)
	private List<Long> orderTableIds;

	protected TableGroupRequest() {
	}

	public TableGroupRequest(LocalDateTime createdDate, List<Long> orderTableIds) {
		this.createdDate = createdDate;
		this.orderTableIds = orderTableIds;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TableGroupRequest that = (TableGroupRequest) o;
		return Objects.equals(createdDate, that.createdDate) && Objects.equals(orderTableIds, that.orderTableIds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdDate, orderTableIds);
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}

	public int getOrderTableIdsSize() {
		return orderTableIds.size();
	}

	public TableGroup toTableGroup() {
		return new TableGroup();
	}
}
