package kitchenpos.tablegroup.dto;

import kitchenpos.ValidateBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@DisplayName("테이블 그룹 요청")
public class TableGroupRequestTest extends ValidateBase {
    @DisplayName("테이블이 비어있는 경우")
    @Test
    public void emptyTable() {
        TableGroupRequest request = new TableGroupRequest(Collections.emptyList());
        validate(request);
    }
}