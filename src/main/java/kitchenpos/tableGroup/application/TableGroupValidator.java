package kitchenpos.tableGroup.application;

import kitchenpos.tableGroup.dto.OrderTableIdRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class TableGroupValidator {

    public void validateOrderTableSize(List<OrderTableIdRequest> orderTableIdRequests, int savedOrderSize) {
        if (CollectionUtils.isEmpty(orderTableIdRequests) || orderTableIdRequests.size() < 2) {
            throw new IllegalArgumentException();
        }

        if (orderTableIdRequests.size() != savedOrderSize) {
            throw new IllegalArgumentException();
        }
    }


}
