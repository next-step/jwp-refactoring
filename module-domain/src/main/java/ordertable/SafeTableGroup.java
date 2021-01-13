package kitchenpos.domain.ordertable;

public interface SafeTableGroup {
    void canChangeEmptyStatus(Long orderTableId);
    Long getTableGroupId(Long orderTableId);
}
