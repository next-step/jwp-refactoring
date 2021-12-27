package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }


    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public OrderTable group(Long tableGroupId) {
        if (!isEmpty() || this.tableGroupId != null) {
            throw new IllegalArgumentException("단체 지정하려는 테이블 중 비어있지 않거나 이미 단체 지정된 테이블이 있습니다.");
        }
        this.tableGroupId = tableGroupId;
        this.empty = false;
        return this;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }
}
