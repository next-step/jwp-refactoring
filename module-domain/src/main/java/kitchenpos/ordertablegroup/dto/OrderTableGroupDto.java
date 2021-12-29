package kitchenpos.ordertablegroup.dto;

import java.time.LocalDateTime;

import kitchenpos.ordertablegroup.domain.OrderTableGroup;

public class OrderTableGroupDto {
	private Long id;
	private LocalDateTime createdDate;

	public OrderTableGroupDto() {
	}

	public OrderTableGroupDto(Long id, LocalDateTime createdDate) {
		this.id = id;
		this.createdDate = createdDate;
	}

	public static OrderTableGroupDto from(OrderTableGroup orderTableGroup) {
		OrderTableGroupDto dto = new OrderTableGroupDto();
		dto.id = orderTableGroup.getId();
		dto.createdDate = orderTableGroup.getCreatedDate();
		return dto;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
}
