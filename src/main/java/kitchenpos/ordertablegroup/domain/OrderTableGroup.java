package kitchenpos.ordertablegroup.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.ordertable.domain.OrderTable;

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

	@OneToMany(mappedBy = "orderTableGroup")
	private List<OrderTable> orderTables = new ArrayList<>();

	protected OrderTableGroup() {
	}

	public static OrderTableGroup of(List<OrderTable> orderTables) {
		OrderTableGroup orderTableGroup = new OrderTableGroup();
		orderTableGroup.orderTables = orderTables;
		orderTableGroup.orderTables.forEach(orderTable -> orderTable.setOrderTableGroup(orderTableGroup));
		return orderTableGroup;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}
}
