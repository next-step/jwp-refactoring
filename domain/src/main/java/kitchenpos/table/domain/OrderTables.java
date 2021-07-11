package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.exception.NotChangeStatusException;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroupId", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderTable> data = new ArrayList<>();

    protected OrderTables() { }

    public void add(OrderTable orderTable) {
        data.add(orderTable);
    }

    public void ungroup() {

        if (CollectionUtils.isEmpty(data)) {
            return;
        }

        if (hasNotCompletedOrder()) {
            throw new NotChangeStatusException("요리 중이거나 식사 중인 주문이 있으면 단체 지정 해제할 수 없습니다.");
        }

        data.forEach(OrderTable::ungroup);
        data.clear();
    }

    private boolean hasNotCompletedOrder() {
        return data.stream().anyMatch(OrderTable::hasCookingOrMealOrder);
    }

    public int size() {
        return data.size();
    }

    public List<OrderTable> getData() {
        return Collections.unmodifiableList(data);
    }
}
