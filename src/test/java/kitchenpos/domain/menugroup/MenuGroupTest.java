package kitchenpos.domain.menugroup;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.exception.EmptyNameException;

class MenuGroupTest {

    @DisplayName("MenuGroup 은 Name 으로 생성된다.")
    @ParameterizedTest
    @ValueSource(strings = {"가", "가나", "가나12", "가나12ab"})
    void creat1(String name) {
        // when & then
        assertThatNoException().isThrownBy(() -> MenuGroup.from(name));
    }

    @DisplayName("MenuGroup 생성 시, Name 이 null 또는 빈 문자열이면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void creat2(String name) {
        // when & then
        assertThrows(EmptyNameException.class, () -> MenuGroup.from(name));
    }
}