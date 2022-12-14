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
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;


    protected OrderTable() {

    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
        this.tableGroup = null;
    }

    public void includeTo(TableGroup tableGroup) {
        if (!isEmpty()) {
            throw new IllegalArgumentException("해당 테이블은 빈 테이블이 아닙니다.");
        }
        if (this.tableGroup != null) {
            throw new IllegalArgumentException("해당 테이블에는 테이블 그룹이 존재합니다.");
        }
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        if (Objects.isNull(this.tableGroup)) {
            throw new IllegalArgumentException("그룹 테이블이 존재하지 않습니다.");
        }
        this.tableGroup = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("주문한 테이블이 빈 테이블이면 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests.change(numberOfGuests);
    }

    public void changeEmptyStatus(boolean empty) {
        if (this.tableGroup != null) {
            throw new IllegalArgumentException("테이블 그룹이 존재하면 테이블 상태를 변경할 수 없습니다.");
        }
        this.empty = empty;
    }


    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }

}
