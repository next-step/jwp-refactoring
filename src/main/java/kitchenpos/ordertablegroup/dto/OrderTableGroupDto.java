package kitchenpos.ordertablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ordertable.dto.OrderTableDto;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;

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

	public static OrderTableGroupDto from(OrderTableGroup orderTableGroup) {
		OrderTableGroupDto dto = new OrderTableGroupDto();
		dto.id = orderTableGroup.getId();
		dto.createdDate = orderTableGroup.getCreatedDate();
		dto.orderTables = orderTableGroup.getOrderTables()
			.stream()
			.map(OrderTableDto::from)
			.collect(Collectors.toList());
		return dto;
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
