package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("이름 관련")
public class NameTest {

    @Test
    @DisplayName("이름이 null 일수 없다.")
    void createName() {
        // when
        String name = null;
        // then
        assertThatThrownBy(() -> Name.of(name)).isInstanceOf(IllegalArgumentException.class);
    }
}
