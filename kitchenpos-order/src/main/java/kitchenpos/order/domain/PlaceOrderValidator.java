package kitchenpos.order.domain;

public interface PlaceOrderValidator {
    void validateTableEmpty(Long tableId);
}
