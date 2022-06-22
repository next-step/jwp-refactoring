package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_table")
public class OrderTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTableEntity() {
    }

    private OrderTableEntity(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private OrderTableEntity(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableEntity of(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTableEntity(tableGroupId, numberOfGuests, empty);
    }

    public static OrderTableEntity of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTableEntity(id, tableGroupId, numberOfGuests, empty);
    }

    public void mapIntoGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroupId);
    }

    public void validateHasTableGroupId() {
        if (isGrouped()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateIsNotEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
