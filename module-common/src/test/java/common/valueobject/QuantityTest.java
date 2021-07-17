package common.valueobject;

import common.valueobject.exception.NegativeQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuantityTest {

    @Test
    void create() {
        //when
        Quantity quantity = Quantity.of(10L);

        //then
        assertThat(quantity.getValue()).isEqualTo(10L);
    }

    @DisplayName("갯수는 0보다 작을 수 없습니다.")
    @Test
    void createException() {
        //when
        assertThatThrownBy(() -> Quantity.of(-1L))
                .isInstanceOf(NegativeQuantityException.class); //then
    }
}
