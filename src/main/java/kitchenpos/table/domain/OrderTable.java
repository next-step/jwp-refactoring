package kitchenpos.table.domain;

import kitchenpos.ExceptionMessage;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    protected OrderTable() {}

    public OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException(ExceptionMessage.EXIST_TABLE_GROUP.getMessage());
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuests newNumberOfGuests) {
        if (this.empty) {
            throw new IllegalArgumentException(ExceptionMessage.EMPTY_TABLE.getMessage());
        }
        this.numberOfGuests = newNumberOfGuests;
    }

    public void group(TableGroup tableGroup) {
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void validateTableGroup() {
        if(!this.empty) {
            throw new IllegalArgumentException(ExceptionMessage.NOT_EMPTY_TABLE.getMessage());
        }
        if(Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException(ExceptionMessage.EXIST_TABLE_GROUP.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

}
