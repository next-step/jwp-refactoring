package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        if(tableGroup != null) {
            tableGroup.addOrderTables(this);
        }
    }

    public int getNumberOfGuests() {
        this.validateNumberOfGuests();
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.validateNumberOfGuests();
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    /**
     * 테이블의 손님 수가 적합한지 확인합니다.
     */
    private void validateNumberOfGuests() {
        if (this.numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 테이블그룹화 되어 있는 테이블인지 확인합니다.
     */
    public void notExistTabeGroup() {
        if (Objects.nonNull(this.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }
}
