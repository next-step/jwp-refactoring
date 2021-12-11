package kitchenpos.table.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.Assert;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "tableGroup")
    private List<OrderTable> tables;

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> tables) {
        Assert.notNull(tables, "주문 테이블 리스트는 필수입니다.");
        Assert.noNullElements(tables,
            () -> String.format("주문 테이블 리스트(%s)에 null이 포함될 수 없습니다.", tables));
        this.tables = tables;
    }

    public static OrderTables from(List<OrderTable> tables) {
        return new OrderTables(tables);
    }

    public List<OrderTable> list() {
        return Collections.unmodifiableList(tables);
    }

    boolean hasSizeMoreThan(int size) {
        return tables.size() > size;
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
