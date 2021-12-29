package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.exception.IllegalMenuPriceException;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.exception.LimitPriceException;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import kitchenpos.product.exception.ProductNotFoundException;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static kitchenpos.menu.fixtures.MenuFixtures.양념치킨두마리메뉴;
import static kitchenpos.product.fixtures.ProductFixtures.양념치킨;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.menu.application
 * fileName : MenuValidatorTest
 * author : haedoang
 * date : 2021/12/26
 * description :
 */
@DisplayName("메뉴 Validator 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    @ParameterizedTest(name = "value: " + ParameterizedTest.ARGUMENTS_PLACEHOLDER)
    @ValueSource(ints = {-10, -5, -1})
    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다: int")
    public void createFailByPrice(int candidate) {
        // then
        assertThatThrownBy(() -> new Menu(
                "두마리메뉴",
                new BigDecimal(candidate),
                1L,
                Lists.newArrayList(
                        new MenuProduct(1L, 2L)
                )
        )).isInstanceOf(IllegalMenuPriceException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다: null")
    public void createFailByPriceNull() {
        // then
        assertThatThrownBy(() -> new Menu(
                "두마리메뉴",
                null,
                null,
                null)
        ).isInstanceOf(IllegalMenuPriceException.class);
    }

    @Test
    @DisplayName("메뉴의 가격은 메뉴상품들의 수량과 가격의 합과 일치하여야 한다.")
    public void createFailByMenusPrices() {
        // given
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(anyLong())).willReturn(Optional.of(양념치킨()));

        // when
        Menu menu = new Menu(
                "가격불일치메뉴",
                new BigDecimal(Long.MAX_VALUE),
                1L,
                Lists.newArrayList(
                        new MenuProduct(1L, 2L)
                ));
        // then
        assertThatThrownBy(() -> menuValidator.validate(menu)).isInstanceOf(LimitPriceException.class);
    }

    @Test
    @DisplayName("메뉴그룹이 등록되어 있어야 한다.")
    public void createFail() {
        // given
        given(menuGroupRepository.existsById(any())).willThrow(MenuGroupNotFoundException.class);

        // then
        assertThatThrownBy(() -> menuValidator.validate(양념치킨두마리메뉴())).isInstanceOf(MenuGroupNotFoundException.class);
    }

    @Test
    @DisplayName("메뉴상품은 상품이 등록되어 있어야 한다.")
    public void createFailByMenuProduct() {
        // given
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(anyLong())).willThrow(ProductNotFoundException.class);

        // then
        assertThatThrownBy(() -> menuValidator.validate(양념치킨두마리메뉴())).isInstanceOf(ProductNotFoundException.class);
    }

}
