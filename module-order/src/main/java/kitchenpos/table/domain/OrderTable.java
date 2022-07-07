package kitchenpos.table.domain;

import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tableGroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private Empty empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, new NumberOfGuests(numberOfGuests), new Empty(empty));
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(id, tableGroup, new NumberOfGuests(numberOfGuests), new Empty(empty));
    }

    public OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, Empty empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(OrderTableRequest request) {
        return new OrderTable(request.getNumberOfGuests(), request.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        changeNumberOfGuests(new NumberOfGuests(numberOfGuests));
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        if (numberOfGuests.lessThenZero()) {
            throw new IllegalArgumentException("손님수는 0명 미만으로 설정할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty.value();
    }

    public Empty getEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public void changeEmpty(boolean empty) {
        changeEmpty(new Empty(empty));
    }

    public void changeEmpty(Empty empty) {
        this.empty = empty;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTableGroup(), that.getTableGroup()) && Objects.equals(getNumberOfGuests(), that.getNumberOfGuests()) && Objects.equals(isEmpty(), that.isEmpty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTableGroup(), getNumberOfGuests(), isEmpty());
    }
}
