package kitchenpos.domain.orderTable;

public interface SafeOrder {
    void canChangeEmptyStatus(Long orderTableId);
}
