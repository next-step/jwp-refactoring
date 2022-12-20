package kitchenpos.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static kitchenpos.constants.ErrorCodeType.GUEST_NOT_NULL_AND_ZERO;
import static kitchenpos.constants.ErrorCodeType.TABLE_GROUP_NOT_NULL;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;

    @Column(name = "empty")
    private boolean isEmpty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public OrderTable(int numberOfGuests, boolean isEmpty) {
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void changeEmpty(boolean isEmpty) {
        validCheckIsNotNullTableGroup();
        this.isEmpty = isEmpty;
    }


    private void validCheckIsNotNullTableGroup() {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException(TABLE_GROUP_NOT_NULL.getMessage());
        }
    }

    public void addTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean isNotNull() {
        return this.tableGroup != null;
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        validCheckIsGuestZero(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    private void validCheckIsGuestZero(Integer numberOfGuests) {
        if (numberOfGuests < 0 || Objects.isNull(numberOfGuests)) {
            throw new IllegalArgumentException(GUEST_NOT_NULL_AND_ZERO.getMessage());
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
        return isEmpty;
    }

}
