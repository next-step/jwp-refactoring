package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "table_group")
public class TableGroup2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    private LocalDateTime createdDate = LocalDateTime.now();

    protected TableGroup2() {
    }

    public TableGroup2(List<OrderTable> orderTables) {
        checkOrderTables(orderTables);
        orderTables.forEach(this::addOrderTable);
    }

    private void checkOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블을 그룹화 하려면 2개 이상의 테이블이 필요합니다.");
        }
    }

    TableGroup2(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void addOrderTable(OrderTable orderTable) {
        checkOrderTable(orderTable);
        orderTable.setEmpty(false);
        orderTable.setTableGroup(this);
        orderTables.add(orderTable);
    }

    private void checkOrderTable(OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블만 그룹화 할 수 있습니다.");
        }

        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException("테이블 그룹에 포함되어 있습니다.");
        }
    }

    public void ungroupAll() {
        orderTables.forEach(OrderTable::ungroup);
    }

    // TODO 이하 삭제

    public void setId(Long tableGroupId) {
        this.id = tableGroupId;
    }
}
