package kitchenpos.order.domain;

public interface OrderValidator {
    void validateEmptyTable(Long orderTableId);
}
