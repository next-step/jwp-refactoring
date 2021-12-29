package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.ordertable.domain.OrderTable;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint(20)")
	private Long id;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	public TableGroup() {
	}

	public TableGroup(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

}
