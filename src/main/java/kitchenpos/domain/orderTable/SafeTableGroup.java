package kitchenpos.domain.orderTable;

public interface SafeTableGroup {
    void canChangeEmptyStatus(Long orderTableId);
}
