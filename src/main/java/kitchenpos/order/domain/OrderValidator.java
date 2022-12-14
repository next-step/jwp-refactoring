package kitchenpos.order.domain;

public interface OrderValidator {
    void checkOrderTableIsNotEmpty(Long orderTableId);
}
