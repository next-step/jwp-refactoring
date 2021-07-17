package kitchenpos.order.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import kitchenpos.table.domain.OrderTable;

@Entity
public class Order {
	private Long id;

	private OrderTable orderTable;

	private String orderStatus;

	private LocalDateTime orderedTime;
}
