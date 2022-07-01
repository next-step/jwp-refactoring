package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private OrderTableNumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    public OrderTable() {

    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = new OrderTableNumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void updateEmpty(boolean empty) {
        valdateForUpdateEmpty();
        this.empty = empty;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        valdateForUpdateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = new OrderTableNumberOfGuests(numberOfGuests);
    }

    private void valdateForUpdateEmpty() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체 지정된 테이블 입니다.");
        }
    }

    private void valdateForUpdateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문 손님 수는 0 미만일 수 없습니다.");
        }

        if (empty) {
            throw new IllegalArgumentException("비어있는 테이블 입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmptyAndNotExistTableGroup() {
        return !empty || Objects.nonNull(tableGroup);
    }

    public void updateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }
}
