package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    public static final int MINIMUM_GROUP_NUMBER = 2;
    
    private List<OrderTable> elements = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        elements = new ArrayList<>(orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_GROUP_NUMBER) {
            throw new IllegalArgumentException("2개 이상의 테이블을 단체 지정할 수 있습니다.");
        }
    }

    public List<OrderTable> elements() {
        return Collections.unmodifiableList(elements);
    }
}
