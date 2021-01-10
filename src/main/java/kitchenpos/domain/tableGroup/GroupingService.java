package kitchenpos.domain.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.exceptions.InvalidTryGroupingException;
import kitchenpos.domain.tableGroup.exceptions.InvalidTableGroupTryException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupingService {
    public TableGroup group(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        try {
            orderTables.forEach(it -> it.group(tableGroup.getId()));

            return tableGroup;
        } catch (InvalidTryGroupingException e) {
            throw new InvalidTableGroupTryException("이미 단체 지정된 주문 테이블을 또 단체 지정할 수 없습니다.");
        }
    }
}
