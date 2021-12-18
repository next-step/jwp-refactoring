package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTableGroupDto {
	private Long id;
	private LocalDateTime createdDate;
	private List<OrderTableDto> orderTables;

	public OrderTableGroupDto() {
	}

	public OrderTableGroupDto(Long id, LocalDateTime createdDate,
		List<OrderTableDto> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTableDto> getOrderTables() {
		return orderTables;
	}
}
