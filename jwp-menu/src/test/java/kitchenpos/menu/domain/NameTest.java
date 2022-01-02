package kitchenpos.menu.domain;

import common.domain.Name;
import common.exception.EmptyNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("이름 도메인 테스트")
class NameTest {

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(Name.from("이름")).isEqualTo(Name.from("이름"));
    }

    @DisplayName("비어있는 문자열은 안된다")
    @Test
    void validateTest() {
        assertThatThrownBy(() -> Name.from(""))
                .isInstanceOf(EmptyNameException.class);
    }
}
