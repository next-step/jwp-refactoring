package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.TableGroup;
import kitchenpos.exception.NotFoundException;

public class TableGroupResponse {

	private Long id;
	private LocalDateTime createdDate;
	private List<OrderTableResponse> orderTables;

	private TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public static TableGroupResponse of(TableGroup tableGroup) {
		if (tableGroup == null) {
			throw new NotFoundException("단체테이블 정보를 찾을 수 없습니다.");
		}
		List<OrderTableResponse> orderTables = tableGroup.getOrderTables()
			.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
		return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}
}
