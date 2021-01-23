package kitchenpos.ui;

import kitchenpos.application.DomainTestUtils;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class MenuRestControllerTest extends DomainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Product 후라이드 = productService.findById(1l);
        Product 양념치킨 = productService.findById(2l);
        메뉴상품_후라이드 = new MenuProduct(후라이드.getId(), 1);
        메뉴상품_양념치킨 = new MenuProduct(양념치킨.getId(), 1);
        후라이드양념반반메뉴 = 메뉴그룹을_생성한다("후라이드양념반반메뉴");
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() throws Exception {
        Menu 후라이드양념반반 = 메뉴를_생성한다(후라이드양념반반메뉴, "후라이드양념반반", 32000, 메뉴상품_후라이드, 메뉴상품_양념치킨);
        String body = objectMapper.writeValueAsString(후라이드양념반반);
        컨트롤러_생성_요청_및_검증(mockMvc, MENU_URI, body);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void search() throws Exception {
        컨트롤러_조회_요청_및_검증(mockMvc, MENU_URI);
    }
}