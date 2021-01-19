package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity(name = "table_group")
public class TableGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime createdDate;

	protected TableGroup() {
	}

	public static TableGroup newInstance() {
		return new TableGroup();
	}

	@PrePersist
	public void prePersist() {
		this.createdDate = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
}
