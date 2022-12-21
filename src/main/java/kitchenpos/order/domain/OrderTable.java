package kitchenpos.order.domain;

import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

import static kitchenpos.constants.ErrorCodeType.GUEST_NOT_NULL_AND_ZERO;
import static kitchenpos.constants.ErrorCodeType.TABLE_GROUP_NOT_NULL;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    @Column(name = "empty")
    private boolean isEmpty;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public OrderTable(int numberOfGuests, boolean isEmpty) {
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void changeEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void addTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean isNotNull() {
        return this.tableGroup != null;
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
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
        return isEmpty;
    }

}
