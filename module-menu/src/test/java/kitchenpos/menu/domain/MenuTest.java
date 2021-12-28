package kitchenpos.menu.domain;

import static kitchenpos.menu.application.fixture.MenuProductFixture.메뉴상품_치킨_리스트;
import static kitchenpos.menugroup.application.fixture.MenuGroupFixture.메뉴그룹_치킨류;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidParameterException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Menu 클래스")
class MenuTest {

    @Nested
    @DisplayName("of 메소드는")
    class Describe_of {

        @Nested
        @DisplayName("만약 마이너스 금액이 주어진다면")
        class Context_with_minus {

            private final int minusPrice = -15000;

            @Test
            @DisplayName("`InvalidParameterException` 에러가 발생한다.")
            void it_return_exception() {
                // Todo menuGroup.getId()
                ThrowableAssert.ThrowingCallable actual = () -> Menu.of("메뉴", minusPrice,
                    메뉴그룹_치킨류().getId(), 메뉴상품_치킨_리스트());

                assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
            }
        }
    }
}
