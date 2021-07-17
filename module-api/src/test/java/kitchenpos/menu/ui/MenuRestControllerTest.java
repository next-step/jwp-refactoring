package kitchenpos.menu.ui;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.ui.ControllerTest;
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
    private MenuGroupService menuGroupService;

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

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() throws Exception {
        MenuRequest menuRequest = 메뉴를_생성한다(32000, 후라이드양념반반메뉴);
        String body = objectMapper.writeValueAsString(menuRequest);

        컨트롤러_생성_요청_및_검증(MENU_URI, body);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void search() throws Exception {
        컨트롤러_조회_요청_및_검증(MENU_URI);
    }

    private MenuRequest 메뉴를_생성한다(int price, MenuGroup menuGroup) {
        Menu menu = new Menu("후라이드양념반반", BigDecimal.valueOf(price), menuGroup);
        후라이드 = new MenuProduct(menu.getId(), 후라이드상품, 1);
        양념치킨 = new MenuProduct(menu.getId(), 양념치킨상품, 1);
        menu.updateMenuProducts(Arrays.asList(후라이드, 양념치킨));
        return MenuRequest.of(menu);
    }
}