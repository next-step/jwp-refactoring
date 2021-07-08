package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.MERGE)
    private final List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(final OrderTable requireOrderTable1, final OrderTable requireOrderTable2, final OrderTable ...orderTables) {
        this(null, requireOrderTable1, requireOrderTable2, orderTables);
    }

    public TableGroup(Long id, final OrderTable requireOrderTable1, final OrderTable requireOrderTable2, final OrderTable ...orderTables) {
        if (requireOrderTable1 == null || requireOrderTable2 == null) {
            throw new IllegalArgumentException("주문테이블 최소 갯수는 2개입니다.");
        }

        this.id = id;
        appendOrderTables(requireOrderTable1, requireOrderTable2, orderTables);
    }

    private void appendOrderTables(final OrderTable orderTable1, final OrderTable orderTable2, final OrderTable ...orderTables) {
        //TODO
        List<OrderTable> list = Arrays.asList(orderTable1, orderTable2);
        list.addAll(Arrays.asList(orderTables));

        list.stream()
            .distinct()
            .forEach(orderTable -> {
                this.orderTables.add(orderTable);
                orderTable.setTableGroup(this);
            });
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void changeEmpty(final boolean empty) {
        orderTables.stream()
                .forEach(orderTable -> orderTable.changeEmpty(empty));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
