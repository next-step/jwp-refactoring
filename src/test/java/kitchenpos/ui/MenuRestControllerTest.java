package kitchenpos.ui;

import static kitchenpos.ui.MenuGroupRestControllerTest.메뉴_그룹_생성_요청;
import static kitchenpos.ui.ProductRestControllerTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuRestControllerTest extends BaseTest {
    private final Product 상품 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
    private final MenuGroup 메뉴_그룹 = new MenuGroup(1L, "한마리메뉴");
    private final List<MenuProduct> 메뉴_항목 = Arrays.asList(new MenuProduct(상품.getId(), 1));
    private final Menu 메뉴 = new Menu("후라이드치킨", BigDecimal.valueOf(16000), 메뉴_그룹.getId(), 메뉴_항목);

    @Test
    void 생성() {
        메뉴_그룹_생성_요청(메뉴_그룹);
        상품_생성_요청(상품);

        ResponseEntity<Menu> response = 메뉴_생성_요청(메뉴);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 조회() {
        메뉴_그룹_생성_요청(메뉴_그룹);
        상품_생성_요청(상품);
        메뉴_생성_요청(메뉴);

        ResponseEntity<List<Menu>> response = 조회_요청();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    public static ResponseEntity<Menu> 메뉴_생성_요청(Menu menu) {
        return testRestTemplate.postForEntity(basePath + "/api/menus", menu, Menu.class);
    }

    private ResponseEntity<List<Menu>> 조회_요청() {
        return testRestTemplate.exchange(
                basePath + "/api/menus",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Menu>>() {});
    }
}
