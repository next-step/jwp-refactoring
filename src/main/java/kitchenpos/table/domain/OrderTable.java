package kitchenpos.table.domain;

import kitchenpos.tableGroup.domain.TableGroup;

import javax.persistence.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {}

    public OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(NumberOfGuests numberOfGuests) {
        this(null, null, numberOfGuests, true);
    }

    public void updateEmpty(boolean empty) {
        this.empty = empty;
    }

    public void updateNumberOfGuests(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public void joinGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void isPossibleChangeNumberOfGuests() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 자리일 때 손님 수를 변경할 수 없습니다.");
        }
    }

    public void leaveGroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }
}
