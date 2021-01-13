package kitchenpos.domain.order;

public interface SafeOrderTable {
    void canOrderAtThisTable(Long tableId);
}
