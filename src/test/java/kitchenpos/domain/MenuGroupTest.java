package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.domain.NameTest.EMPTY_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("메뉴 그룹 테스트")
public class MenuGroupTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        final String name = "추천 메뉴";
        assertThat(MenuGroup.from(name)).isEqualTo(MenuGroup.from(name));
    }

    @DisplayName("생성 예외 - 이름이 빈 값인 경우")
    @Test
    void 생성_예외_이름이_빈_값인_경우() {
        final String name = EMPTY_NAME;
        assertThatIllegalArgumentException().isThrownBy(() -> MenuGroup.from(name));
    }
}
