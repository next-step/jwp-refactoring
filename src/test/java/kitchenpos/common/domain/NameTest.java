package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.error.ErrorEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NameTest {
    @Test
    void 이름을_생성한다() {
        Name name = new Name("이름");
        assertThat(name.value()).isEqualTo("이름");
    }

    @Test
    void 이름은_NULL일_수_없습니다() {
        assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_NULL_OR_EMPTY_NAME.message());
    }

    @DisplayName("이름은 빈 값일 수 없다.")
    @Test
    void 이름은_비어있을_수_없습니다() {
        assertThatThrownBy(() -> new Name(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_NULL_OR_EMPTY_NAME.message());
    }
}
