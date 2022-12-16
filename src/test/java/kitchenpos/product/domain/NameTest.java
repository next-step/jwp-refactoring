package kitchenpos.product.domain;

import kitchenpos.product.domain.Name;
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
        //given:
        final String name = "아무 이름";
        //when, then:
        assertThat(Name.from(name)).isEqualTo(Name.from(name));
    }

    @DisplayName("생성 예외 - 이름이 빈 값일 경우")
    @Test
    void 생성_예외_이름이_빈_값일_경우() {
        //given:
        final String name = EMPTY_NAME;
        //when, then:
        assertThatIllegalArgumentException().isThrownBy(() -> Name.from(name));
    }
}
