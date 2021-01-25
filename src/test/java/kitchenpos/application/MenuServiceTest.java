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

    private MenuGroup 후라이드양념반반메뉴;
    private Product 후라이드상품;
    private Product 양념치킨상품;
    private MenuProduct 후라이드;
    private MenuProduct 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드양념반반메뉴 = new MenuGroup("후라이드양념반반메뉴");
        후라이드상품 = productService.findById(1l);
        양념치킨상품 = productService.findById(2l);

    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        Menu menu = new Menu( "후라이드양념반반", BigDecimal.valueOf(32000), 후라이드양념반반메뉴);
        후라이드 = new MenuProduct(menu, 후라이드상품, 1);
        양념치킨 = new MenuProduct(menu, 양념치킨상품, 1);

        menu.updateMenuProducts(Arrays.asList(후라이드, 양념치킨));
        assertAll(
                () -> assertThat(menu.getId()).isNotNull(),
                () -> assertThat(menu.getName()).isEqualTo("후라이드양념반반")
        );
    }

    @DisplayName("메뉴를 생성한다 : 가격이 0미만이면 익셉션 발생")
    @Test
    void createPriceException() {
        assertThatThrownBy(() -> new Menu( "후라이드양념반반", BigDecimal.valueOf(-1), 후라이드양념반반메뉴))
                .isInstanceOf(MenuException.class);
    }

    @DisplayName("메뉴를 생성한다 : menuGroupId가 존재하지 않으면 익셉션 발생")
    @Test
    void createMenuGroupIdException() {
        assertThatThrownBy(() -> new Menu( "후라이드양념반반", BigDecimal.valueOf(32000), new MenuGroup("없는그룹")))
                .isInstanceOf(MenuException.class);
    }

    @DisplayName("메뉴를 생성한다 : 메뉴의 각 상품들 가격의 합이 메뉴의 가격보다 크면 익셉션 ")
    @Test
    void comparePriceMenuProductsException() {
        assertThatThrownBy(() -> new Menu( "후라이드양념반반", BigDecimal.valueOf(-1), 후라이드양념반반메뉴))
                .isInstanceOf(MenuException.class);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void list() {
        List<Menu> menus = menuService.list();
        assertThat(menus.size()).isGreaterThanOrEqualTo(1);
    }
}