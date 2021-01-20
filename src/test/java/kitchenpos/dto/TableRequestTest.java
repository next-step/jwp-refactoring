package kitchenpos.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableRequestTest extends ValidateBase {
    @DisplayName("고객 수가 음수인 경우")
    @Test
    public void validateGuests() {
        TableRequest request = TableRequest.builder()
                .numberOfGuests(-1)
                .build();

        validate(request);
    }
}