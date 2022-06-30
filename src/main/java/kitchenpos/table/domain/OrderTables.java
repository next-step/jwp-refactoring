package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> list;

    public OrderTables() {
        list = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTables) {
        list = orderTables;
    }

    public List<OrderTable> getList() {
        return list;
    }
}
