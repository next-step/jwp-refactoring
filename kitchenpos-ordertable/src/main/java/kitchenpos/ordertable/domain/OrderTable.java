package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.TableChangeNumberOfGuestsException;
import kitchenpos.ordertable.exception.TableUpdateStateException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.ordertable.vo.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    @Column(nullable = false)
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean orderClose = false;

    protected OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(boolean orderClose) {
        this.orderClose = orderClose;
    }

    public OrderTable(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean orderClose) {
        this(null, null, numberOfGuests, orderClose);
    }

    public OrderTable(Long id, NumberOfGuests numberOfGuests, boolean orderClose) {
        this(id, null, numberOfGuests, orderClose);
    }

    public OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests,
        boolean orderClose) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.orderClose = orderClose;
    }

    public void updateTableStatus(boolean orderClose) {
        if (Objects.nonNull(tableGroupId)) {
            throw new TableUpdateStateException();
        }
        this.orderClose = orderClose;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        if (isOrderClose()) {
            throw new TableChangeNumberOfGuestsException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void groupIn(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        changeOrderClose(false);
    }

    public boolean hasGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public void changeOrderClose(final boolean orderClose) {
        this.orderClose = orderClose;
    }

    public boolean isOrderClose() {
        return orderClose;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public int getNumberOfGuestsVal() {
        return numberOfGuests.getNumberOfGuests();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof OrderTable)) {
            return false;
        }

        OrderTable that = (OrderTable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
