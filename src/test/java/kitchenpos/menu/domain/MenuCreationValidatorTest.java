package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.application.fixture.MenuGroupDtoFixtureFactory;
import kitchenpos.menu.domain.fixture.MenuProductFixtureFactory;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.product.application.fixture.ProductDtoFixtureFactory;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuCreationValidatorTest {

    private MenuCreationValidator menuCreationValidator;

    @Mock
    private ProductRepository productRepository;

    private MenuGroup menuGroup;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        menuCreationValidator = new MenuCreationValidator(productRepository);

        menuGroup = MenuGroupDtoFixtureFactory.createMenuGroup("메뉴그룹1");
        product1 = ProductDtoFixtureFactory.createProduct(1L, "상품1", 1000);
        product2 = ProductDtoFixtureFactory.createProduct(2L, "상품2", 2000);
    }

    @Test
    @DisplayName("메뉴가격이 음수인 경우 메뉴 등록 실패")
    void 메뉴그룹에_메뉴추가_가격이_음수인경우() {
        String menuName = "메뉴1";
        int menuPrice = -1 * 1000;
        Menu menu = 테스트_메뉴_생성(menuGroup, menuName, menuPrice);
        assertThatThrownBy(() -> menuCreationValidator.validate(menu))
                .isInstanceOf(InvalidMenuPriceException.class);
    }

    @Test
    @DisplayName("메뉴가격이 상품가격의 합보다 큰 경우 메뉴 등록 실패")
    void 메뉴그룹에_메뉴추가_가격이_상품가격의_합보다_큰경우() {
        Mockito.when(productRepository.findAllById(Lists.newArrayList(product1.getId(), product2.getId())))
                .thenReturn(Lists.newArrayList(product1, product2));
        String menuName = "메뉴1";
        int menuPrice = 10000;
        Menu menu = 테스트_메뉴_생성(menuGroup, menuName, menuPrice);
        assertThatThrownBy(() -> menuCreationValidator.validate(menu))
                .isInstanceOf(InvalidMenuPriceException.class);
    }

    private Menu 테스트_메뉴_생성(MenuGroup menuGroup, String menuName, int menuPrice) {
        MenuProduct menuProduct1 = MenuProductFixtureFactory.createMenuProduct(1L, product1.getId(), 4);
        MenuProduct menuProduct2 = MenuProductFixtureFactory.createMenuProduct(2L, product2.getId(), 1);
        return new Menu(menuName, new BigDecimal(menuPrice), menuGroup, Lists.newArrayList(menuProduct1, menuProduct2));
    }
}
