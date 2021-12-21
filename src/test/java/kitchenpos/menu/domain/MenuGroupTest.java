package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.fixture.NameFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 그룹 테스트")
class MenuGroupTest {
    @DisplayName("메뉴 그룹 생성 확인")
    @Test
    void 메뉴_그룹_생성_확인() {
        // given
        Name 메뉴_그룹_이름 = NameFixture.of("추천메뉴");

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuGroup.of(메뉴_그룹_이름);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("메뉴 그룹 이름은 비어있을 수 없음")
    @Test
    void 메뉴_그룹_이름은_비어있을_수_없음() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuGroup.of(null);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
