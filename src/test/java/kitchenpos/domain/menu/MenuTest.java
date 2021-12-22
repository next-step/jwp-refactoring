package kitchenpos.domain.menu;

import static kitchenpos.application.fixture.MenuGroupFixture.메뉴그룹_치킨류;
import static kitchenpos.application.fixture.MenuProductFixture.메뉴상품_치킨_리스트;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.InvalidParameterException;
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
                ThrowableAssert.ThrowingCallable actual = () -> Menu.of("메뉴", minusPrice,
                    메뉴그룹_치킨류(), 메뉴상품_치킨_리스트());

                assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
            }
        }

        @Nested
        @DisplayName("`메뉴`의 가격이 `메뉴상품`목록의 전체 가격(상품가격 * 갯수)의 합보다 크면")
        class Context_with_over_menu_price {

            private final int largePrice = 1500000000;

            @Test
            @DisplayName("`InvalidParameterException` 에러가 발생한다.")
            void it_return_exception() {
                ThrowableAssert.ThrowingCallable actual = () -> Menu.of("메뉴", largePrice,
                    메뉴그룹_치킨류(), 메뉴상품_치킨_리스트());

                assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
            }
        }

        @Nested
        @DisplayName("`메뉴`가 속할 `메뉴그룹`이 필수로 있어야 한다.")
        class Context_without_menuGroup {

            MenuGroup menuGroup = null;

            @Test
            @DisplayName("`InvalidParameterException` 에러가 발생한다.")
            void it_return_exception() {
                ThrowableAssert.ThrowingCallable actual = () -> Menu.of("메뉴", 15000,
                    menuGroup, 메뉴상품_치킨_리스트());

                assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
            }
        }
    }

}