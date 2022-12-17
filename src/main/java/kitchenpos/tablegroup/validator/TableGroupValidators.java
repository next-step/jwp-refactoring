package kitchenpos.tablegroup.validator;

import java.util.List;
import org.springframework.context.ApplicationEventPublisher;

public interface TableGroupValidators {

    void validateCreation(Long tableGroupId, ApplicationEventPublisher eventPublisher, List<Long> orderTableIds);

    void validateUngroup(Long tableGroupId);
}
