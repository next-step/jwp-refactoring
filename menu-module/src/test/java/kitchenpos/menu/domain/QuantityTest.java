package kitchenpos.menu.domain;

import kitchenpos.exception.IllegalQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("수량 원시값 포장 객체 테스트")
class QuantityTest {


    @Test
    void 수량_객체_생성() {
        Quantity quantity = new Quantity(1);
        assertThat(quantity).isEqualTo(new Quantity(1));
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void 수량_객체_생성시_1보다_작은값_입력하게되면_에러발생(long quantity) {
        assertThatThrownBy(() -> new Quantity(quantity)).isInstanceOf(IllegalQuantityException.class);
    }
}
