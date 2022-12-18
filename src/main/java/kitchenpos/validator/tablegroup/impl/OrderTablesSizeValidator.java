package kitchenpos.validator.tablegroup.impl;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.validator.tablegroup.TablesGroupValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderTablesSizeValidator implements TablesGroupValidator {

    @Override
    public void validate(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("최소 2개 이상의 주문 테이블들에 대해서만 단체 지정이 가능합니다");
        }
    }
}
