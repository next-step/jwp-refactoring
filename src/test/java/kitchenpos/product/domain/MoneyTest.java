package kitchenpos.product.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MoneyTest {

    @DisplayName("입력하는 돈이 없으면 예외발생")
    @Test
    public void throwsExceptionWhenNullAmount(){
        assertThatThrownBy(() -> Money.of(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("입력하는 돈이 0보다작으면 예외발생")
    @Test
    public void throwsExceptionWhenNetativeAmount(){
        assertThatThrownBy(() -> Money.of(-1000l))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
