package kitchenpos.application;

import kitchenpos.advice.exception.MenuException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    private MenuProduct 메뉴상품_후라이드;
    private MenuProduct 메뉴상품_양념치킨;
    private MenuGroup 후라이드양념반반메뉴;

    @BeforeEach
    void setUp() {
        Product 후라이드 = productService.findById(1l);
        Product 양념치킨 = productService.findById(2l);
        메뉴상품_후라이드 = new MenuProduct(후라이드.getId(), 1);
        메뉴상품_양념치킨 = new MenuProduct(양념치킨.getId(), 1);
        후라이드양념반반메뉴 = 메뉴그룹을_생성한다("후라이드양념반반메뉴");
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        final Menu menu = 메뉴를_생성한다(후라이드양념반반메뉴, "후라이드양념반반", 32000, 메뉴상품_후라이드, 메뉴상품_양념치킨);
        assertAll(
                () -> assertThat(menu.getId()).isNotNull(),
                () -> assertThat(menu.getName()).isEqualTo("후라이드양념반반")
        );
    }

    @DisplayName("메뉴를 생성한다 : 가격이 0미만이면 익셉션 발생")
    @Test
    void createPriceException() {
        assertThatThrownBy(() -> 메뉴를_생성한다(후라이드양념반반메뉴, "후라이드양념반반", -1, 메뉴상품_후라이드, 메뉴상품_양념치킨))
                .isInstanceOf(MenuException.class);
    }

    @DisplayName("메뉴를 생성한다 : menuGroupId가 존재하지 않으면 익셉션 발생")
    @Test
    void createMenuGroupIdException() {
        assertThatThrownBy(() -> 메뉴를_생성한다(new MenuGroup(), "후라이드양념반반", -1, 메뉴상품_후라이드, 메뉴상품_양념치킨))
                .isInstanceOf(MenuException.class);
    }

    @DisplayName("메뉴를 생성한다 : 메뉴의 각 상품들 가격의 합이 메뉴의 가격이 같지 않으면 익셉션 ")
    @Test
    void comparePriceMenuProductsException() {
        assertThatThrownBy(() -> 메뉴를_생성한다(후라이드양념반반메뉴, "후라이드양념반반", 34000, 메뉴상품_후라이드, 메뉴상품_양념치킨))
                .isInstanceOf(MenuException.class);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void list() {
        List<Menu> menus = menuService.list();
        assertThat(menus.size()).isGreaterThanOrEqualTo(1);
    }

    private Menu 메뉴를_생성한다(MenuGroup menuGroup, String name, int price, MenuProduct... products) {
        Menu menu = new Menu(name, BigDecimal.valueOf(price), menuGroup.getId(), Arrays.asList(products));
        return menuService.create(menu);
    }

    private MenuGroup 메뉴그룹을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupService.create(menuGroup);
    }
}