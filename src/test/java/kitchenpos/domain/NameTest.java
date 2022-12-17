package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.InvalidNameException;

class NameTest {

    @DisplayName("[이름] 빈 문자열이나 널값일 수 없다")
    @Test
    void test1() {
        assertThatThrownBy(() -> new Name("   "))
            .isInstanceOf(InvalidNameException.class);
        assertThatThrownBy(() -> new Name(null))
            .isInstanceOf(InvalidNameException.class);
        assertDoesNotThrow(() -> new Name("test"));
    }
}
