package kitchenpos.menu.application;

import kitchenpos.advice.exception.MenuException;
import kitchenpos.advice.exception.MenuGroupException;
import kitchenpos.advice.exception.PriceException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    private MenuGroup 후라이드양념반반메뉴;
    private Product 후라이드상품;
    private Product 양념치킨상품;
    private MenuProduct 후라이드;
    private MenuProduct 양념치킨;

    @BeforeEach
    void setUp() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("후라이드양념반반메뉴");
        후라이드양념반반메뉴 = menuGroupService.create(menuGroupRequest);
        후라이드상품 = productService.findById(1l);
        양념치킨상품 = productService.findById(2l);

    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        MenuRequest menuRequest = 메뉴를_생성한다(32000, 후라이드양념반반메뉴);
        Menu savedMenu = menuService.create(menuRequest);

        assertAll(
                () -> assertThat(savedMenu.getMenuProducts()).contains(후라이드, 양념치킨),
                () -> assertThat(savedMenu.getName()).isEqualTo("후라이드양념반반")
        );
    }

    @DisplayName("메뉴를 생성한다 : 가격이 0미만이면 익셉션 발생")
    @Test
    void createPriceException() {
        assertThatThrownBy(() -> 메뉴를_생성한다(-1, 후라이드양념반반메뉴))
                .isInstanceOf(PriceException.class);
    }

    @DisplayName("메뉴를 생성한다 : menuGroupId가 존재하지 않으면 익셉션 발생")
    @Test
    void createMenuGroupIdException() {
        MenuRequest menuRequest = 메뉴를_생성한다(32000, new MenuGroup(100l, "메뉴그룹없음"));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(MenuGroupException.class);
    }

    @DisplayName("메뉴를 생성한다 : 메뉴의 각 상품들 가격의 합이 메뉴의 가격보다 크면 익셉션 ")
    @Test
    void comparePriceMenuProductsException() {
        assertThatThrownBy(() -> 메뉴를_생성한다(38000, 후라이드양념반반메뉴))
                .isInstanceOf(MenuException.class);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    @Transactional
    void list() {
        List<Menu> menus = menuService.list();
        assertThat(menus.size()).isGreaterThanOrEqualTo(1);
    }

    private MenuRequest 메뉴를_생성한다(int price, MenuGroup menuGroup) {
        Menu menu = new Menu("후라이드양념반반", BigDecimal.valueOf(price), menuGroup);
        후라이드 = new MenuProduct(menu, 후라이드상품, 1);
        양념치킨 = new MenuProduct(menu, 양념치킨상품, 1);
        menu.updateMenuProducts(Arrays.asList(후라이드, 양념치킨));
        return MenuRequest.of(menu);
    }
}