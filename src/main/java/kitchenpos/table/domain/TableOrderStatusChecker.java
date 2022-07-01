package kitchenpos.table.domain;

import java.util.List;

public interface TableOrderStatusChecker {
    boolean isExistTablesBeforeBillingStatus(List<Long> orderTableIds);

    boolean isBeforeBillingStatus(Long orderTableId);
}
