package kitchenpos.table.domain.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kitchenpos.table.domain.tablegroup.TableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUEST = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.tableGroup = null;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);
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

    public void changeTableGroup(TableGroup tableGroup) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("그룹 테이블이 존재합니다.");
        }
        validateOrderTableEmpty();
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void changeEmptyTableGroup() {
        if (Objects.isNull(this.tableGroup)) {
            throw new IllegalArgumentException("그룹 테이블이 존재하지 않습니다.");
        }
        this.tableGroup = null;
    }

    public void changeEmpty() {
        this.empty = true;
        this.numberOfGuests = 0;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUEST) {
            throw new IllegalArgumentException("방문자 수가 잘못되었습니다.");
        }
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUEST) {
            throw new IllegalArgumentException("방문자 수가 잘못되었습니다.");
        }
    }

    private void validateOrderTableEmpty() {
        if (!this.empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않습니다.");
        }
    }
}
