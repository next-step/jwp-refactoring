package kitchenpos.order.application;

public interface OrderOrderTableValidator {
    void validateExistsOrderTableByIdAndEmptyIsFalse(Long orderTableId);
}
