package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

	private Long id;
	private LocalDateTime createdDate;

	public TableGroupResponse() {
	}

	public TableGroupResponse(long id, LocalDateTime createdDate) {
		this.id = id;
		this.createdDate = createdDate;
	}

	public static TableGroupResponse of(TableGroup tableGroup) {
		return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate());
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
}
