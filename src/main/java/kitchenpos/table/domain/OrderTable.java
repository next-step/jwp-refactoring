package kitchenpos.table.domain;

import kitchenpos.global.BaseTimeEntity;
import kitchenpos.table.exception.TableNotAvailableException;
import kitchenpos.tablegroup.exception.TableGroupNotAvailableException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20)
    private Long id;

    @Column(name = "table_group_id", length = 20, nullable = true)
    private Long tableGroup;

    @Column(name = "number_of_guests", length = 11, nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", length = 1, nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long tableGroup, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.tableGroup = tableGroup;
    }

    public void checkAvailability() {
        if (this.empty) {
            throw new TableNotAvailableException(String.format("table id is %d", this.id));
        }
    }

    public void checkAvailabilityTableGroup() {
        if (!this.empty || Objects.nonNull(this.tableGroup)) {
            throw new TableGroupNotAvailableException(String.format("table id is %d", this.id));
        }
    }

    public void changeEmpty(boolean empty, OrderTableValidator orderTableValidator) {
        orderTableValidator.changeEmptyValidator(this);
        this.empty = empty;
    }

    public void changeNumberOfGuest(final int numberOfGuests) {
        checkAvailability();
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup(OrderTableValidator orderTableValidator) {
        orderTableValidator.ungroupValidator(this);
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

}
