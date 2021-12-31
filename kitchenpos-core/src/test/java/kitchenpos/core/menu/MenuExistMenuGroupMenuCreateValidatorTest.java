package kitchenpos.core.menu;

import kitchenpos.MenuFixture;
import kitchenpos.core.menu.domain.Menu;
import kitchenpos.core.menugroup.domain.MenuExistMenuGroupMenuCreateValidator;
import kitchenpos.core.menugroup.domain.MenuGroupRepository;
import kitchenpos.core.product.domain.Product;
import kitchenpos.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 존재 유효성 검사 테스트")
@ExtendWith(MockitoExtension.class)
class MenuExistMenuGroupMenuCreateValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuExistMenuGroupMenuCreateValidator createValidator;

    private Product 양지쌀국수;
    private Product 분짜;

    @BeforeEach
    void setUp() {
        양지쌀국수 = ProductFixture.getProduct(1L, "양지쌀국수", 7_500);
        분짜 = ProductFixture.getProduct(2L, "분짜", 9_500);
    }

    @DisplayName("존재하지 않을 경우 유효하지 못하다.")
    @Test
    void validateFail() {
        // given
        Menu menu = MenuFixture.getMenu(1L, "추천메뉴",
                17_000,
                1L,
                Arrays.asList(
                        MenuFixture.getMenuProduct(1L, 양지쌀국수, 10),
                        MenuFixture.getMenuProduct(2L, 분짜, 6)
                ));
        given(menuGroupRepository.existsById(anyLong())).willReturn(false);
        // when
        final ThrowableAssert.ThrowingCallable throwingCallable = () -> createValidator.validate(menu);
        // then
        assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않을 경우 유효하다..")
    @Test
    void validateSuccess() {
        // given
        Menu menu = MenuFixture.getMenu(1L, "추천메뉴",
                17_000,
                1L,
                Arrays.asList(
                        MenuFixture.getMenuProduct(1L, 양지쌀국수, 10),
                        MenuFixture.getMenuProduct(2L, 분짜, 6)
                ));
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        // when
        final Executable executable = () -> createValidator.validate(menu);
        // then
        assertDoesNotThrow(executable);
    }
}
