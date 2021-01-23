package kitchenpos.dto;

import kitchenpos.table.dto.TableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 요청")
public class TableRequestTest extends ValidateBase {
    @DisplayName("고객 수가 음수인 경우")
    @Test
    public void validateGuests() {
        TableRequest request = new TableRequest(-1);

        validate(request);
    }
}