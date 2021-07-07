package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "order_table")
@Entity
public class OrderTableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "table_group_id")
  private Long tableGroupId;

  @Embedded
  private NumberOfGuests numberOfGuests;

  @Column(name = "empty", nullable = false)
  private Boolean empty;

  protected OrderTableEntity() {
  }

  public OrderTableEntity(Integer numberOfGuests, Boolean empty) {
    this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    this.empty = empty;
  }

  public OrderTableEntity(Long tableGroupId, Integer numberOfGuests, Boolean empty) {
    this.tableGroupId = tableGroupId;
    this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    this.empty = empty;
  }

  public Long getId() {
    return id;
  }

  public Long getTableGroupId() {
    return tableGroupId;
  }

  public Integer getNumberOfGuests() {
    return numberOfGuests.getValue();
  }

  public Boolean isEmpty() {
    return empty;
  }

  public void changeEmpty(boolean empty) {
    if (Objects.nonNull(tableGroupId)) {
      throw new IllegalArgumentException();
    }
    this.empty = empty;
  }

  public void changeNumberOfGuests(int numberOfGuests) {
    if (empty) {
      throw new IllegalArgumentException();
    }
    this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
  }
}
