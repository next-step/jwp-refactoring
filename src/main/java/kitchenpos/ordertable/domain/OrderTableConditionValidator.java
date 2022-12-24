package kitchenpos.ordertable.domain;

public interface OrderTableConditionValidator {
    void checkNotCompletedOrderExist(OrderTable orderTable);
}
