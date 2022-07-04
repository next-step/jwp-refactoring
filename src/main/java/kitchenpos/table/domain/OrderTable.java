package kitchenpos.table.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(Long id, boolean empty) {
        this.id = id;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(boolean empty, TableGroup tableGroup) {
        this.empty = empty;
        this.tableGroup = tableGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(this.getTableGroup())) {
            throw new BadRequestException(ErrorCode.ORDER_TABLE_GROUPED);
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new BadRequestException(ErrorCode.NEGATIVE_NUMBER_OF_GUESTS);
        }

        if (this.isEmpty()) {
            throw new BadRequestException(ErrorCode.TABLE_EMPTY);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void updateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void validate() {
        if (!this.isEmpty() || Objects.nonNull(this.getTableGroup())) {
            throw new BadRequestException(ErrorCode.ORDER_TABLE_GROUPED);
        }
    }
}
