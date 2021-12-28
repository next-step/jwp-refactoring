package kitchenpos.menu.domain;

import static kitchenpos.menu.application.fixture.MenuProductFixture.메뉴상품_치킨_리스트;
import static kitchenpos.menugroup.application.fixture.MenuGroupFixture.메뉴그룹_치킨류;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import kitchenpos.exception.InvalidParameterException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Menu 클래스")
@ExtendWith(MockitoExtension.class)
class MenuTest {

    @Mock
    private MenuValidator menuValidator;

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
                    메뉴그룹_치킨류().getId(), 메뉴상품_치킨_리스트());

                assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
            }
        }
    }

    @Test
    @DisplayName("registerMenu 는 메뉴생성검증을 호출 해야한다.")
    void menuValidator() {
        // given
        Menu menu = Menu.of("메뉴", 15000, 메뉴그룹_치킨류().getId(), 메뉴상품_치킨_리스트());

        // when
        menu.registerMenu(menuValidator);

        // then
        verify(menuValidator).validateRegister(menu);
    }
}
