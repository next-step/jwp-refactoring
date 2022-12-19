package kitchenpos.tablegroup.validator;

import java.util.List;

public interface TableGroupValidators {

    void validateCreation(List<Long> orderTableIds);

    void validateUngroup(Long tableGroupId);
}
