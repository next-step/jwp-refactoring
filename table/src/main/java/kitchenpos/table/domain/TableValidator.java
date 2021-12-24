package kitchenpos.table.domain;

public interface TableValidator {
    public OrderTable getValidatedOrderTableForChangeEmpty(Long orderTableId);

    public OrderTable getValidatedOrderTableForChangeNumberOfGuests(Long orderTableId, int numberOfGuests);
}
