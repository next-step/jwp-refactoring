package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Name 클래스 테스트")
class NameTest {

    private final String name = "강정치킨";

    @DisplayName("'강정치킨'인 Name를 생성한다.")
    @Test
    void create() {
        Name name = new Name(this.name);
        assertThat(name.getValue()).isEqualTo(this.name);
    }
}
