package kitchenpos.table.domain;

public interface OrderTableLinker {
    void validateOrderStatusByOrderTableId(Long orderTableId);
}
