package kitchenpos.ui;

import static kitchenpos.ui.MenuGroupRestControllerTest.메뉴_그룹_생성_요청;
import static kitchenpos.ui.ProductRestControllerTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.BaseTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuRestControllerTest extends BaseTest {
    private final ProductRequest 상품 = new ProductRequest("후라이드", BigDecimal.valueOf(16000));
    private MenuProductRequest 메뉴_항목;
    private final MenuGroup 메뉴_그룹 = new MenuGroup("한마리메뉴");
    private MenuRequest 메뉴_요청;

    @BeforeEach
    void beforeEach() {
        ResponseEntity<ProductResponse> productResponse = 상품_생성_요청(상품);
        메뉴_항목 = new MenuProductRequest(productResponse.getBody().getId(), 1);
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

        ResponseEntity<List<MenuResponse>> response = 조회_요청();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    public static ResponseEntity<MenuResponse> 메뉴_생성_요청(MenuRequest menuRequest) {
        return testRestTemplate.postForEntity(basePath + "/api/menus", menuRequest, MenuResponse.class);
    }

    private ResponseEntity<List<MenuResponse>> 조회_요청() {
        return testRestTemplate.exchange(
                basePath + "/api/menus",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MenuResponse>>() {});
    }
}
