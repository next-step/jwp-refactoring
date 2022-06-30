package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private Boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final Boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalStateException("빈 테이블이어서 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void changeEmpty(final boolean empty) {
        if (tableGroup != null) {
            throw new IllegalStateException("단체 지정되어 빈 테이블로 설정할 수 없습니다.");
        }
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean hasGroup() {
        return tableGroup != null;
    }
}
