package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

class MenuRestControllerTest extends ControllerTest {

    private final String MENU_URI = "/api/menus";

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    private Menu menu;
    private MenuProduct 메뉴상품_후라이드;
    private MenuProduct 메뉴상품_양념치킨;
    private MenuGroup 후라이드양념반반메뉴;

    @BeforeEach
    void setUp() {
        후라이드양념반반메뉴 = 메뉴그룹을_생성한다("후라이드양념반반메뉴");
        menu = 메뉴를_생성한다(후라이드양념반반메뉴, "후라이드양념반반", 32000);
        Product 후라이드 = productService.findById(1l);
        Product 양념치킨 = productService.findById(2l);
        메뉴상품_후라이드 = new MenuProduct(menu, 후라이드, 1);
        메뉴상품_양념치킨 = new MenuProduct(menu, 양념치킨, 1);
        menu.updateMenuProducts(Arrays.asList(메뉴상품_후라이드,메뉴상품_양념치킨));
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() throws Exception {
        String body = objectMapper.writeValueAsString(menu);
        컨트롤러_생성_요청_및_검증(MENU_URI, body);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void search() throws Exception {
        컨트롤러_조회_요청_및_검증(MENU_URI);
    }

    private Menu 메뉴를_생성한다(MenuGroup menuGroup, String name, int price, MenuProduct... products) {
        Menu menu = new Menu(name, BigDecimal.valueOf(price), menuGroup, Arrays.asList(products));
        return menuService.create(menu);
    }

    private MenuGroup 메뉴그룹을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupService.create(menuGroup);
    }
}