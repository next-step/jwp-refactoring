package kitchenpos.table.domain;

public interface TableValidator {
  void validateTableInUse(Long orderTableId);
}
