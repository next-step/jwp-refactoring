package kitchenpos.ordertable.domain;

public interface OrderStatusCheckService {
    boolean existsOrdersInProgress(Long orderTableId);
}
