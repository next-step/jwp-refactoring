package kitchenpos.ordertablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "table_group")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OrderTableGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	protected OrderTableGroup() {
	}

	public static OrderTableGroup newInstance() {
		return new OrderTableGroup();
	}

	public static OrderTableGroup from(Long id) {
		OrderTableGroup orderTableGroup = new OrderTableGroup();
		orderTableGroup.id = id;
		return orderTableGroup;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void group(List<Long> orderTableIds, OrderTableGroupValidator validator) {
		group(orderTableIds, validator, null);
	}

	public void group(
		List<Long> orderTableIds,
		OrderTableGroupValidator validator,
		ApplicationEventPublisher publisher
	) {
		validator.validateOrderTablesAreGreaterThanOrEqualToTwo(orderTableIds);
		validator.validateNotGrouped(orderTableIds);
		validator.validateOrderTableIsEmpty(orderTableIds);

		if (publisher != null) {
			publisher.publishEvent(new OrderTableGroupingEvent(id, orderTableIds));
		}
	}

	public void ungroup(OrderTableGroupValidator validator) {
		ungroup(validator, null);
	}

	public void ungroup(OrderTableGroupValidator validator, ApplicationEventPublisher publisher) {
		validator.validateNotCompletedOrderNotExist(id);

		if (publisher != null) {
			publisher.publishEvent(new OrderTableUngroupingEvent(id));
		}
	}
}
