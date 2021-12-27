package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "table_group")
public class TableGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup")
	private List<OrderTable> orderTables;

	protected TableGroup() {
	}

	private TableGroup(Long id, List<OrderTable> orderTables) {
		this.id = id;
		this.orderTables = orderTables;
	}

	public static TableGroup of(Long id, List<OrderTable> orderTables) {
		return new TableGroup(id, orderTables);
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
