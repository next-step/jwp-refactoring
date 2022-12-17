package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderTable> orderTables = new ArrayList<>();

    public List<OrderTable> value() {
        return orderTables;
    }

}
