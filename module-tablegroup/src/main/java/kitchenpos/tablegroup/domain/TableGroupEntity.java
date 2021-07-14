package kitchenpos.tablegroup.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "table_group")
@Entity
public class TableGroupEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_date", nullable = false)
  private LocalDateTime createdDate = LocalDateTime.now();

  public TableGroupEntity() {
    this.createdDate = LocalDateTime.now();
  }

  public TableGroupEntity(Long id, LocalDateTime createdDate) {
    this.id = id;
    this.createdDate = createdDate;
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TableGroupEntity that = (TableGroupEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdDate);
  }
}
