package kitchenpos.table.domain;

import kitchenpos.tableGroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void unGroupingTable() {
        if(Objects.nonNull(tableGroup) && tableGroup.hasContain(this)) {
            tableGroup.removeTable(this);
        }
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

    public void checkGrouping() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("그룹핑된 상태입니다.");
        }
    }

    public void changeStatus(boolean empty) {
        this.empty = empty;
    }

    public void checkEmpty() {
        if (empty) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수가 올바르지 않습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void initialTableGroup(TableGroup tableGroup) {
        if(!tableGroup.getOrderTables().hasContain(this)) {
            tableGroup.addTable(this);
        }
        this.tableGroup = tableGroup;
        this.empty = false;
    }
}
