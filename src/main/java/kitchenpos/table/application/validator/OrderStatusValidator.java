package kitchenpos.table.application.validator;

public interface OrderStatusValidator {
    void existsByOrderTableIdAndOrderStatusIn(Long orderTableId);
}
