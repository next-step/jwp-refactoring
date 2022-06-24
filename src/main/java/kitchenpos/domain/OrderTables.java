package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    // entity 기본생성자 이므로 사용 금지
    protected OrderTables() {
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.isEmpty()) {
            throw new IllegalArgumentException("테이블이 존재하지 않습니다.");
        }

        orderTables.forEach(this::addOrderTable);

        if (orderTables.size() <= 1) {
            throw new IllegalArgumentException("테이블이 2개이상 존재해야 합니다.");
        }
    }

    private void addOrderTable(OrderTable orderTable) {
        if (orderTable == null) {
            throw new IllegalArgumentException("테이블이 존재하지 않습니다.");
        }

        this.orderTables.add(orderTable);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}
