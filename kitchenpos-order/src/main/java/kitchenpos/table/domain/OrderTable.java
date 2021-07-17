package kitchenpos.table.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {
    private static final String TABLE_GROUP_EXIST = "단체 지정에 포함되어 있습니다.";
    private static final int MINIMUM_GUEST_NUMBER = 0;
    private static final String GUEST_NOT_EXIST = "변경할 손님이 존재 하지 않습니다.";
    private static final String TABLE_EMPTY = "테이블이 비어있는 상태입니다";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private Integer numberOfGuests;

    private Boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Integer numberOfGuests, Boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, Integer numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(Boolean empty) {
        validateEmpty();
        this.empty = empty;
    }

    private void validateEmpty() {
        if (checkTableGroupExist()) {
            throw new EntityExistsException(TABLE_GROUP_EXIST);
        }
    }

    private boolean checkTableGroupExist() {
        return Objects.nonNull(tableGroupId);
    }

    public boolean checkIsNotValidTableGroup() {
        return !empty || checkTableGroupExist();
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(Integer numberOfGuests) {
        if (Objects.nonNull(numberOfGuests) && numberOfGuests < MINIMUM_GUEST_NUMBER) {
            throw new IllegalArgumentException(GUEST_NOT_EXIST);
        }
        if (isEmpty()) {
            throw new IllegalStateException(TABLE_EMPTY);
        }
    }

    public void assignTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderTable)) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", tableGroupId=" + tableGroupId +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
