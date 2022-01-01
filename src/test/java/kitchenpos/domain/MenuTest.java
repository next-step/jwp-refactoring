package kitchenpos.domain;

import kitchenpos.common.exceptions.EmptyMenuGroupException;
import kitchenpos.common.exceptions.EmptyNameException;
import kitchenpos.common.exceptions.GreaterProductSumPriceException;
import kitchenpos.fixture.TestMenuFactory;
import kitchenpos.fixture.TestMenuGroupFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 도메인 테스트")
class MenuTest {
    @DisplayName("등록 시, 메뉴 그룹이 필요하다")
    @Test
    void validateTest() {
        assertThatThrownBy(() -> TestMenuFactory.메뉴_생성(null, "메뉴", 1000))
                .isInstanceOf(EmptyMenuGroupException.class);
    }

    @DisplayName("등록 시, 이름이 필요하다")
    @Test
    void validateTest3() {
        final MenuGroup 메뉴그룹 = TestMenuGroupFactory.메뉴그룹_생성("메뉴그룹");

        assertThatThrownBy(() -> TestMenuFactory.메뉴_생성(메뉴그룹, null, 1000))
                .isInstanceOf(EmptyNameException.class);
    }
}
