package kitchenpos.domain;

import kitchenpos.dto.OrderTableRequestDto;

import javax.persistence.*;

@Entity
public class OrderTable {

    private static final int MIN_GUEST = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(OrderTableRequestDto request) {
        this(null, null, request.getNumberOfGuests(), request.isEmpty());
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
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

    public void changeEmpty() {
        this.empty = true;
    }

    public void changeNumberOfGuest(int numberOfGuests) {
        checkChangeable(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void checkChangeable(int numberOfGuests) {
        if (numberOfGuests < MIN_GUEST) {
            throw new IllegalArgumentException();
        }
        if (this.empty) {
            throw new IllegalArgumentException();
        }
    }

    public void group(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
    }
}
