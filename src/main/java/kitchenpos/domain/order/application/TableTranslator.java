package kitchenpos.domain.order.application;

import kitchenpos.domain.order.domain.OrderTableVO;

public interface TableTranslator {

    OrderTableVO getOrderTable(Long orderTableId);
}
