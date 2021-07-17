package kitchenpos.tableGroup.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;

@Entity
public class TableGroup {
	private Long id;

	private LocalDateTime createdDate;
}
