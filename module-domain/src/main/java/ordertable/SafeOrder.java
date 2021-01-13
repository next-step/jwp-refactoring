package kitchenpos.domain.ordertable;

public interface SafeOrder {
    void canChangeEmptyStatus(Long orderTableId);
}
