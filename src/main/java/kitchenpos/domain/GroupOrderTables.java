package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class GroupOrderTables {

    @OneToMany(fetch = FetchType.LAZY)
    List<OrderTable> orderTables = new ArrayList<>();

    protected GroupOrderTables() {

    }

    private GroupOrderTables(List<OrderTable> orderTables) {
        validGroup(orderTables);
        this.orderTables = orderTables;
    }


    public static GroupOrderTables of(List<OrderTable> orderTables) {
        return new GroupOrderTables(orderTables);
    }

    public void group(Long tableGroupId) {
        this.orderTables.forEach((it) -> it.setTableGroupId(tableGroupId));
    }

    public void unGroup() {
        orderTables.forEach((it)-> it.changeTableGroupId(null));
    }

    private void validGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체지정은 2개이상 테이블만 가능합니다.");
        }
        if (isNotAbleGroup(orderTables)) {
            throw new IllegalArgumentException("빈테이블이 존재하거나. 이미 단체석인경우에는 단체석으로 지정할수 없습니디");
        }
    }


    private boolean isNotAbleGroup(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch((it) -> it.isEmpty() || Objects.nonNull(it.getTableGroupId()));
    }

    public List<OrderTable> getTables() {
        return Collections.unmodifiableList(orderTables);
    }

}
