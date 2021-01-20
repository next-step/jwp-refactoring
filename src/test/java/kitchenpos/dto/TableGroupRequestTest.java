package kitchenpos.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class TableGroupRequestTest extends ValidateBase {
    @DisplayName("테이블이 비어있는 경우")
    @Test
    public void emptyTable() {
        TableGroupRequest request = new TableGroupRequest();
        validate(request);
    }
}