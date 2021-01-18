package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TableRequest {
    @Positive
    private int numberOfGuests;

    @Builder
    public TableRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
