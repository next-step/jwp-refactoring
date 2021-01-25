package kitchenpos.table.domain;

import kitchenpos.table.exception.InvalidGroupException;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public void group(final TableGroup tableGroup) {
        checkUnGroup();

//        this.tableGroup = tableGroup;
//        this.empty = false;
    }

    private void checkUnGroup() {
//        if (!empty || Objects.nonNull(tableGroup)) {
//            throw new InvalidGroupException("빈 테이블이거나 이미 단체 지정인 경우 단체 지정할 수 없다.");
//        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
