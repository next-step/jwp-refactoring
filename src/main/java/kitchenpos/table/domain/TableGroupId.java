package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TableGroupId {

	@Column(name = "table_group_id")
	private Long id;

	protected TableGroupId() {}

	public TableGroupId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TableGroupId that = (TableGroupId)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
