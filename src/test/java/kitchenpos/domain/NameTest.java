package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("이름 테스트")
public class NameTest {

    public static final String EMPTY_NAME = "";

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        final String name = "아무 이름";
        assertThat(Name.from(name)).isEqualTo(Name.from(name));
    }

    @DisplayName("생성 예외 - 이름이 빈 값일 경우")
    @Test
    void 생성_예외_이름이_빈_값일_경우() {
        final String name = EMPTY_NAME;
        assertThatIllegalArgumentException().isThrownBy(() -> Name.from(name));
    }
}
