package kitchenpos.menu.validator;

import kitchenpos.common.ErrorMessage;
import kitchenpos.menugroup.fixture.MenuGroupTestFixture;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.fixture.MenuProductTestFixture.*;
import static kitchenpos.menu.fixture.MenuTestFixture.메뉴세트요청;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuValidator menuValidator;

    private MenuGroup 중국집1인메뉴세트그룹;
    private MenuProductRequest 짜장면메뉴상품요청;
    private MenuProductRequest 탕수육메뉴상품요청;
    private MenuProductRequest 단무지메뉴상품요청;
    private MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청;
    private MenuProduct 짜장면메뉴상품;
    private MenuProduct 짬뽕메뉴상품;
    private MenuProduct 단무지메뉴상품;

    @BeforeEach
    public void setUp() {
        중국집1인메뉴세트그룹 = MenuGroupTestFixture.중국집1인메뉴세트그룹(MenuGroupTestFixture.중국집1인메뉴세트그룹요청());
        짜장면메뉴상품요청 = 짜장면메뉴상품요청(1L);
        탕수육메뉴상품요청 = 탕수육메뉴상품요청(2L);
        단무지메뉴상품요청 = 단무지메뉴상품요청(4L);
        List<MenuProductRequest> menuProductRequests = Arrays.asList(짜장면메뉴상품요청, 탕수육메뉴상품요청, 단무지메뉴상품요청);
        짜장면_탕수육_1인_메뉴_세트_요청 =
                메뉴세트요청("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 중국집1인메뉴세트그룹.getId(), menuProductRequests);
        짜장면메뉴상품 = 짜장면메뉴상품();
        짬뽕메뉴상품 = 짬뽕메뉴상품();
        단무지메뉴상품 = 단무지메뉴상품();
    }

    @DisplayName("메뉴 가격이 각 상품 가격의 합보다 작으면 IllegalArgumentException을 반환한다.")
    @Test
    void validateCreateMenuWithException1() {
        // given
        MenuProducts menuProducts = MenuProducts.from(Arrays.asList(짜장면메뉴상품, 짬뽕메뉴상품, 단무지메뉴상품));
        when(menuGroupRepository.existsById(any())).thenReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuValidator.validateCreateMenu(짜장면_탕수육_1인_메뉴_세트_요청, menuProducts)
        ).withMessage(ErrorMessage.MENU_PRICE_LESS_THAN_SUM_OF_PRICE.getMessage());
    }

    @DisplayName("메뉴삼품이 존재하지 않으면 IllegalArgumentException을 반환한다.")
    @Test
    void validateCreateMenuWithException2() {
        // given
        MenuProducts menuProducts = MenuProducts.from(Arrays.asList(짜장면메뉴상품, 짬뽕메뉴상품));
        when(menuGroupRepository.existsById(any())).thenReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuValidator.validateCreateMenu(짜장면_탕수육_1인_메뉴_세트_요청, menuProducts)
        ).withMessage(ErrorMessage.INVALID_MENU_PRODUCT.getMessage());
    }
}
