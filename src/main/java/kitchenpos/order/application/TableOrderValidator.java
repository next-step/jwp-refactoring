package kitchenpos.order.application;

public interface TableOrderValidator {
    void validateExistsOrderTableByIdAndEmptyIsFalse(Long orderTableId);
}
