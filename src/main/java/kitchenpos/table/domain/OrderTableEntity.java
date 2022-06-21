package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public static OrderTableEntity of(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTableEntity(tableGroupId, numberOfGuests, empty);
    }

    public void mapIntoGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public boolean isGrouped () {
        return Objects.nonNull(tableGroupId);
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
}
