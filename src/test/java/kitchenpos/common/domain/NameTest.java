package kitchenpos.common.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class NameTest {

    @Test
    void 이름_생성() {
        Name name = new Name("이름");

        assertAll(
                () -> assertThat(name).isNotNull(),
                () -> assertThat(name.getValue()).isEqualTo("이름")
        );
    }

    @Test
    void 이름이_NULL인_경우() {
        assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(RuntimeException.class);
    }

}
