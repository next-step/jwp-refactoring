package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.exception.EmptyNameException;
import kitchenpos.exception.NegativePriceException;

class NameTest {

    @DisplayName("Name 은 1자 이상으로 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"가", "가나", "가나다", "가나다1", "가나다12", "가나다12!"})
    void create1(String name) {
        // when & then
        assertThatNoException().isThrownBy(() -> Name.from(name));
    }

    @DisplayName("Name 을 null 또는 빈 문자열로 만들면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void creat2(String name) {
        // when & then
        assertThrows(EmptyNameException.class, () -> Name.from(name));
    }
}