package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {

    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void checkOrderTableIsEmpty() {
        if (!empty) {
            throw new IllegalArgumentException();
        }
    }

    public void updateTableGroupId(Long tableGroupId) {
        if (this.tableGroupId != tableGroupId) {
            this.tableGroupId = tableGroupId;
        }
    }

    public void changeEmpty(boolean empty, boolean existsOrderStatusComplete) {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException();
        }
        validateOrderStatusIsNotComplete(existsOrderStatusComplete);
        this.empty = empty;
    }

    private void validateOrderStatusIsNotComplete(boolean exists) {
        if (exists) {
            throw new IllegalArgumentException("계산이 완료되지 않았습니다.");
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException();
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        this.tableGroupId = null;
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

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
