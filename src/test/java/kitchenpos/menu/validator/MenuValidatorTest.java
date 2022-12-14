package kitchenpos.menu.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.fixture.TestMenuProductFactory;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 Validator 테스트")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @InjectMocks
    private MenuValidator menuValidator;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    MenuValidatorTest() {}

    @DisplayName("메뉴를 생성할때 정상적으로 유효성 검사가 성공 된다")
    @Test
    void validateCreateMenu() {
        // given
        MenuProductRequest menuProductRequest = MenuProductRequest.of(1L, 1);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(TestMenuProductFactory.create()));
        MenuRequest request = MenuRequest.of(
                "불고기정식",
                BigDecimal.valueOf(8_000),
                1L,
                Arrays.asList(menuProductRequest)
        );

        when(menuGroupRepository.existsById(any())).thenReturn(true);

        // when & then
        assertDoesNotThrow(() -> menuValidator.validateCreateMenu(request, menuProducts));
    }

    @DisplayName("메뉴의 메뉴그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void validateMenuGroupNotExists() {
        // given
        MenuProductRequest menuProductRequest = MenuProductRequest.of(1L, 1);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(TestMenuProductFactory.create()));
        MenuRequest menuRequest = MenuRequest.of(
                "불고기정식",
                BigDecimal.valueOf(8_000),
                1L,
                Arrays.asList(menuProductRequest)
        );

        when(menuGroupRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> menuValidator.validateCreateMenu(menuRequest, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.MENU_GROUP_IS_NOT_EXIST.getMessage());
    }

    @DisplayName("메뉴 상품의 개수가 다르면 예외가 발생한다.")
    @Test
    void validateUnMatchProductCount() {
        // given
        MenuProductRequest menuProductRequest1 = MenuProductRequest.of(1L, 1);
        MenuProductRequest menuProductRequest2 = MenuProductRequest.of(2L, 1);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(TestMenuProductFactory.create()));
        MenuRequest menuRequest = MenuRequest.of(
                "불고기정식",
                BigDecimal.valueOf(8_000),
                1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2)
        );

        when(menuGroupRepository.existsById(any())).thenReturn(true);

        assertThatThrownBy(() -> menuValidator.validateCreateMenu(menuRequest, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.PRODUCT_IS_NOT_EXIST.getMessage());
    }

    @DisplayName("메뉴 상품이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void validateNotExistMenuProducts() {
        // given
        MenuProducts menuProducts = new MenuProducts(new ArrayList<>());
        MenuRequest menuRequest = MenuRequest.of(
                "불고기정식",
                BigDecimal.valueOf(8_000),
                1L,
                new ArrayList<>()
        );

        when(menuGroupRepository.existsById(any())).thenReturn(true);

        assertThatThrownBy(() -> menuValidator.validateCreateMenu(menuRequest, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.MENU_PRODUCT_IS_EMPTY.getMessage());
    }

    @DisplayName("메뉴의 가격이 전체 메뉴상품 가격의 합보다 크면 예외가 발생한다.")
    @Test
    void validateMenuPriceIsBiggerThanTotal() {
        // given
        MenuProductRequest menuProductRequest = MenuProductRequest.of(1L, 1);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(TestMenuProductFactory.create()));
        MenuRequest menuRequest = MenuRequest.of(
                "불고기정식",
                BigDecimal.valueOf(200_000),
                1L,
                Arrays.asList(menuProductRequest)
        );

        when(menuGroupRepository.existsById(any())).thenReturn(true);

        assertThatThrownBy(() -> menuValidator.validateCreateMenu(menuRequest, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.MENU_PRICE_SHOULD_NOT_OVER_TOTAL_PRICE.getMessage());
    }
}
