package kitchenpos.validate;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupValidator {

    public static final int MIN_TABLE_COUNT = 2;

    public void validateRequest(List<TableRequest> tableRequests) {
        if (CollectionUtils.isEmpty(tableRequests) || tableRequests.size() < MIN_TABLE_COUNT) {
            throw new IllegalArgumentException();
        }
        tableRequests.stream()
                .filter(tableRequest -> tableRequest.getId() == null)
                .findFirst()
                .ifPresent(tableRequest -> {
                    throw new IllegalArgumentException();
                });
    }

    public void validateCreate(List<TableRequest> tableRequests, List<OrderTable> orderTables){
        if(tableRequests.size() != orderTables.size()){
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.validateGrouping();
        }
    }
}
