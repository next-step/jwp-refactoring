package kitchenpos.table.domain;

public interface TableExternalValidator {
  void validateTableInUse(Long orderTableId);
}
