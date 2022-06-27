package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTable() {
    }

    public Long getId() {
        return id;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public boolean isGrouped() {
        return !Objects.equals(this.tableGroupId, null);
    }

    public void group(Long tableGroupId) {
        if (!isEmpty() || isGrouped()) {
            throw new IllegalArgumentException("적절하지 않은 테이블이 포함되어 있습니다.");
        }
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }
}
