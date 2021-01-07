package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    private Product product1 = new Product();
    private Product product2 = new Product();
    private MenuProduct menuProduct1 = new MenuProduct();
    private MenuProduct menuProduct2 = new MenuProduct();


    @BeforeEach
    void setup() {
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setId(1L);
        product2.setPrice(BigDecimal.valueOf(100));
        product2.setId(2L);

        menuProduct1.setProductId(product1.getId());
        menuProduct1.setQuantity(1);
        menuProduct2.setProductId(product2.getId());
        menuProduct2.setQuantity(1);
    }

    @DisplayName("올바르지 않은 메뉴 가격으로 메뉴를 등록할 수 없다.")
    @ParameterizedTest
    @NullSource
    @MethodSource("menuCreateFailByInvalidPriceResource")
    void menuCreateFailByInvalidPrice(BigDecimal invalidPrice) {
        // given
        Menu menu = new Menu();
        menu.setPrice(invalidPrice);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }
    public static Stream<Arguments> menuCreateFailByInvalidPriceResource() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of(BigDecimal.valueOf(-2))
        );
    }

    @DisplayName("존재하지 않는 메뉴 그룹으로 메뉴를 등록할 수 없다.")
    @Test
    void createFailWithNotExistMenuGroupTest() {
        // given
        Long notExistMenuGroupId = 1000L;
        Menu withNotExistMenuGroup = new Menu();
        withNotExistMenuGroup.setMenuGroupId(notExistMenuGroupId);
        withNotExistMenuGroup.setPrice(BigDecimal.ONE);

        // when, then
        assertThatThrownBy(() -> menuService.create(withNotExistMenuGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품들 가격의 총합보다 비싸게 메뉴 가격을 정할 수 없다.")
    @Test
    void createFailWithTooExpensivePriceTest() {
        // given
        Long menuGroupId = 1L;
        Menu tooExpensiveMenu = new Menu();
        tooExpensiveMenu.setPrice(BigDecimal.valueOf(1000000));
        tooExpensiveMenu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        tooExpensiveMenu.setMenuGroupId(menuGroupId);
        tooExpensiveMenu.setName("너무 비싼 메뉴");

        // when, then
        assertThatThrownBy(() -> menuService.create(tooExpensiveMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품으로 구성된 메뉴 상품으로 메뉴를 만들 수 없다.")
    @Test
    void createFailWithNotExistProduct() {
        // given
        Long menuGroupId = 1L;

        MenuProduct notExistMenuProduct= new MenuProduct();

        Menu menuWithNotExistProduct = new Menu();
        menuWithNotExistProduct.setPrice(BigDecimal.ONE);
        menuWithNotExistProduct.setMenuGroupId(menuGroupId);
        menuWithNotExistProduct.setMenuProducts(Collections.singletonList(notExistMenuProduct));
        menuWithNotExistProduct.setName("상품이 없는 메뉴");

        // when, then
        assertThatThrownBy(() -> menuService.create(menuWithNotExistProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createMenuTest() {
        // given
        int expectedSize = 2;
        Long menuGroupId = 1L;

        Menu menu = new Menu();
        menu.setPrice(BigDecimal.ONE);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        menu.setName("신메뉴");

        // when
        Menu created = menuService.create(menu);

        // then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getMenuProducts()).hasSize(expectedSize);
    }

    @DisplayName("메뉴 목록을 불러올 수 있다.")
    @Test
    void getMenusTest() {
        // given
        String menuName = "신메뉴";
        Long menuGroupId = 1L;

        Menu menu = new Menu();
        menu.setPrice(BigDecimal.ONE);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        menu.setName(menuName);

        menuService.create(menu);

        // when
        List<Menu> menus = menuService.list();
        Stream<String> names = menus.stream()
                .map(Menu::getName);

        // then
        assertThat(names).contains(menuName);
    }
}
