package kitchenpos.tablegroup.domain;

import java.util.List;

public interface TableGroupExternalValidator {

  void validateTablesInUse(List<Long> tableIds);
}
