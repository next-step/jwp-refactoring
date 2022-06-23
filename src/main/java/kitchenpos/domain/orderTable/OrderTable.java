package kitchenpos.domain.orderTable;

import kitchenpos.domain.tableGroup.TableGroup;
import kitchenpos.dto.orderTable.OrderTableRequest;

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

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    public OrderTable() {

    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable ofCreate(OrderTableRequest orderTableRequest) {
        return new OrderTable(null, null, orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
    }

    public void updateEmpty(boolean empty) {
        valdateForUpdateEmpty();
        this.empty = empty;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        valdateForUpdateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
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

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroup.setId(tableGroupId);
    }

    public TableGroup getTableGroup() {
        return tableGroup;
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
}
