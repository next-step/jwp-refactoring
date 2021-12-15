package kitchenpos.domain;

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

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNonEmptyOrderTable() {
        this.empty = false;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void addTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void checkAvailable() {
        if (!this.isEmpty() || Objects.nonNull(this.getTableGroup())) {
            throw new IllegalArgumentException("주문테이블이 비어있지 않거나, 단체지정이 되어있습니다.");
        }
    }

    public void unGroup() {
        this.tableGroup = null;
    }
}
