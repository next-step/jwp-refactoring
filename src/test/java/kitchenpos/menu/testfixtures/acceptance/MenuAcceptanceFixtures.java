package kitchenpos.menu.testfixtures.acceptance;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.CustomTestRestTemplate;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class MenuAcceptanceFixtures {

    private static final String BASE_URL = "/api/menus";

    public static ResponseEntity<List<MenuResponse>> 메뉴_전체_조회_요청() {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.exchange(BASE_URL, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<MenuResponse>>() {
            });
    }

    public static ResponseEntity<MenuResponse> 메뉴_생성_요청(MenuRequest menuRequest) {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.postForEntity(BASE_URL, menuRequest, MenuResponse.class);
    }

    public static MenuRequest 메뉴_정의(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }

    public static MenuProductRequest 메뉴상품_정의(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
