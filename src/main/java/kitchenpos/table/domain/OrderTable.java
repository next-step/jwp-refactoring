package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "tableGroupId")
    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private TableGroup tableGroup = new TableGroup();

    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if(isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 게스트를 입력할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(boolean empty) {
        if (hasOtherOrderTable()) {
            throw new IllegalArgumentException("단체 지정된 테이블은 빈 테이블 설정/해지할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroup = null;
    }
    public boolean hasOtherOrderTable() {
        return Objects.nonNull(tableGroup);
    }

    public boolean inGroup() {
        return this.tableGroup != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTable that = (OrderTable) o;

        if (numberOfGuests != that.numberOfGuests) return false;
        if (empty != that.empty) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return tableGroup != null ? tableGroup.equals(that.tableGroup) : that.tableGroup == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tableGroup != null ? tableGroup.hashCode() : 0);
        result = 31 * result + numberOfGuests;
        result = 31 * result + (empty ? 1 : 0);
        return result;
    }
}
