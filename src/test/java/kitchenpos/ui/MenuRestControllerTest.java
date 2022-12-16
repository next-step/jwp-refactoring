package kitchenpos.ui;

import static kitchenpos.ui.MenuGroupRestControllerTest.메뉴_그룹_생성_요청;
import static kitchenpos.ui.ProductRestControllerTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuRestControllerTest extends BaseTest {
    private final Product 상품 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
    private final MenuProductRequest 메뉴_항목 = new MenuProductRequest(상품.getId(), 1);
    private final MenuGroup 메뉴_그룹 = new MenuGroup("한마리메뉴");
    private MenuRequest 메뉴_요청;

    @BeforeEach
    void beforeEach() {
        상품_생성_요청(상품);
        ResponseEntity<MenuGroup> menuGroupResponse = 메뉴_그룹_생성_요청(메뉴_그룹);
        메뉴_요청 = new MenuRequest(
                        "후라이드치킨", BigDecimal.valueOf(16000), menuGroupResponse.getBody().getId(), Arrays.asList(메뉴_항목));
    }

    @Test
    void 생성() {
        ResponseEntity<MenuResponse> response = 메뉴_생성_요청(메뉴_요청);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 조회() {
        메뉴_생성_요청(메뉴_요청);

        ResponseEntity<List<Menu>> response = 조회_요청();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    public static ResponseEntity<MenuResponse> 메뉴_생성_요청(MenuRequest menuRequest) {
        return testRestTemplate.postForEntity(basePath + "/api/menus", menuRequest, MenuResponse.class);
    }

    private ResponseEntity<List<Menu>> 조회_요청() {
        return testRestTemplate.exchange(
                basePath + "/api/menus",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Menu>>() {});
    }
}
