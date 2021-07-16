package kitchenpos.table.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_group")
public class TableGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime createdDate;

	public TableGroup() {

	}

	public TableGroup(Long id) {
		this.id = id;
		this.createdDate = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

}
