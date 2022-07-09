package kitchenpos.table.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;

import javax.persistence.Embedded;
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
    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(int numberOfGuests){
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public OrderTable(Long id, boolean empty) {
        this.id = id;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(boolean empty, TableGroup tableGroup) {
        this.empty = empty;
        this.tableGroup = tableGroup;
    }

    public OrderTable(boolean empty, int numberOfGuests) {
        this.empty = empty;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public OrderTable(Long id, boolean empty, int numberOfGuests) {
        this.id = id;
        this.empty = empty;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public OrderTable(long id, boolean empty, int numberOfGuests, TableGroup tableGroup) {
        this.id = id;
        this.empty = empty;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.tableGroup = tableGroup;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void updateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(this.getTableGroup())) {
            throw new BadRequestException(ErrorCode.ORDER_TABLE_GROUPED);
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (this.isEmpty()) {
            throw new BadRequestException(ErrorCode.TABLE_EMPTY);
        }

        this.numberOfGuests.validate(numberOfGuests);
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void validate() {
        if (!this.isEmpty()) {
            throw new BadRequestException(ErrorCode.TABLE_NOT_EMPTY);
        }

        if (Objects.nonNull(this.getTableGroup())) {
            throw new BadRequestException(ErrorCode.ORDER_TABLE_GROUPED);
        }
    }
}
