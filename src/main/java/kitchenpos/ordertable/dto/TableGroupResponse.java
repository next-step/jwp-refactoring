package kitchenpos.ordertable.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import kitchenpos.ordertable.domain.TableGroup;

public class TableGroupResponse {

	private Long id;

	@JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
	private LocalDateTime localDateTime;

	private List<OrderTableResponse> orderTableResponses;

	protected TableGroupResponse() {
	}

	public TableGroupResponse(final Long id, final LocalDateTime localDateTime,
		final List<OrderTableResponse> orderTableResponses) {
		this.id = id;
		this.localDateTime = localDateTime;
		this.orderTableResponses = orderTableResponses;
	}

	public static TableGroupResponse of(final TableGroup tableGroup) {
		if (tableGroup == null) {
			return null;
		}
		return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
			OrderTableResponse.ofList(tableGroup.getOrderTables()));
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public List<OrderTableResponse> getOrderTableResponses() {
		return orderTableResponses;
	}
}
