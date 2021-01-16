package kitchenpos.application.creator;

import java.util.Arrays;
import kitchenpos.dto.TableGroupCreateRequest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class TableGroupHelper {

    public static TableGroupCreateRequest createRequest(Long ...orderTableIds) {
        return new TableGroupCreateRequest(Arrays.asList(orderTableIds));
    }
}
