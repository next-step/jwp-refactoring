package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

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

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(final LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@PrePersist
	public void prePersist() {
		this.createdDate = LocalDateTime.now();
	}

}
