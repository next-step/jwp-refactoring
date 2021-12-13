package kitchenpos.table.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.Assert;

@Embeddable
class OrderTables {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "tableGroup")
    private List<OrderTable> tables;

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> tables) {
        Assert.notNull(tables, "주문 테이블 리스트는 필수입니다.");
        Assert.noNullElements(tables,
            () -> String.format("주문 테이블 리스트(%s)에 null이 포함될 수 없습니다.", tables));
        this.tables = tables;
    }

    static OrderTables from(List<OrderTable> tables) {
        return new OrderTables(tables);
    }

    List<OrderTable> list() {
        return Collections.unmodifiableList(tables);
    }

    boolean notHaveGroupAndEmpty() {
        return tables.stream().allMatch(OrderTable::notHaveGroupAndEmpty);
    }

    boolean anyCookingOrMeal() {
        return tables.stream().anyMatch(OrderTable::isCookingOrMeal);
    }

    int size() {
        return tables.size();
    }

    void changeGroup(TableGroup tableGroup) {
        tables.forEach(table -> table.changeGroup(tableGroup));
    }

    public void ungroup() {
        tables.forEach(OrderTable::ungroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tables);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTables that = (OrderTables) o;
        return Objects.equals(tables, that.tables);
    }

    @Override
    public String toString() {
        return "OrderTables{" +
            "tables=" + tables +
            '}';
    }
}
