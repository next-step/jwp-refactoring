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
	private Long id;

	@Column(nullable = false)
	private LocalDateTime createdDate;

	protected TableGroup() {
	}

	private TableGroup(Long id, LocalDateTime createdDate) {
		this.id = id;
		this.createdDate = createdDate;
	}

	public static TableGroup of(Long id) {
		return new TableGroup(id, LocalDateTime.now());
	}

	public static TableGroup create() {
		return of(null);
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		TableGroup that = (TableGroup)o;

		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
