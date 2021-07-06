package kitchenpos.domain.menu;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.menuproduct.MenuAmountCreateValidator;
import kitchenpos.domain.menuproduct.MenuProductCreate;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.Products;
import kitchenpos.exception.MenuCheapException;
import kitchenpos.exception.ProductNotExistException;
import kitchenpos.fixture.CleanUp;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.양념치킨_1000원;
import static kitchenpos.fixture.ProductFixture.콜라_100원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class MenuTest {
    private List<MenuProductCreate> menuProductCreates;
    private List<Product> productList;

    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();

        this.menuProductCreates = Arrays.asList(
                new MenuProductCreate(1L, 콜라_100원.getId(), 1L),
                new MenuProductCreate(2L, 양념치킨_1000원.getId(), 2L)
        );
        productList = Arrays.asList(콜라_100원, ProductFixture.양념치킨_1000원);
    }

    @Test
    @DisplayName("메듀에 등록할 메뉴와 메뉴 수가 틀리면 ProductNotExistException이 발생한다 ")
    void 메뉴에_등록할_메뉴와_메뉴_수가_틀리면_ProductNotExistException이_발생한다() {
        // given
        MenuCreate menuCreate = new MenuCreate("Hello", new Price(10), null, menuProductCreates);
        List<Product> productList = Arrays.asList(콜라_100원);

        Products products = new Products(productList);

        // when & then
        assertThatExceptionOfType(ProductNotExistException.class)
                .isThrownBy(() -> Menu.create(menuCreate, null, products, new MenuAmountCreateValidator()));
    }

    @Test
    @DisplayName("메뉴의 가격보다 금액이 더 싸면 MenuCheapException이 발생한다")
    void 메뉴의_가격보다_금액이_더_싸면_MenuCheapException이_발생한다() {
        // given
        MenuCreate menuCreate = new MenuCreate(new Name("Hello"), new Price(10000), null, menuProductCreates);

        Products products = new Products(productList);

        // when & then
        assertThatExceptionOfType(MenuCheapException.class)
                .isThrownBy(() -> Menu.create(menuCreate, null, products, new MenuAmountCreateValidator()));
    }

    @Test
    @DisplayName("정상적인 등록")
    void 정상적인_등록() throws Exception {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, new Name("MENU_GROUP"));

        MenuCreate menuCreate = new MenuCreate(new Name("Hello"), new Price(100), null, menuProductCreates);

        Products products = new Products(productList);

        // when
        Menu menu = Menu.create(menuCreate, menuGroup, products, new MenuAmountCreateValidator());

        // when & then
        assertThat(menu.getMenuGroup()).isEqualTo(menuGroup);
        assertThat(menu.getName()).isEqualTo(menuCreate.getName());
        assertThat(menu.getPrice()).isEqualTo(menuCreate.getPrice());
    }
}