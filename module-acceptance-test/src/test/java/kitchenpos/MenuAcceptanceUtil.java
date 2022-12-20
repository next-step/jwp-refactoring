package kitchenpos;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kitchenpos.AcceptanceTest.restTemplate;
import static kitchenpos.MenuGroupAcceptanceTestUtil.메뉴_그룹_등록됨;
import static kitchenpos.ProductAcceptanceTestUtil.상품_등록됨;
import static org.assertj.core.api.Assertions.assertThat;

public final class MenuAcceptanceUtil {

    private MenuAcceptanceUtil() {
    }

    public static MenuResponse 신메뉴_강정치킨_가져오기() {
        ProductResponse 강정치킨 = 상품_등록됨("강정치킨", BigDecimal.valueOf(15_000L));
        MenuGroupResponse 신메뉴 = 메뉴_그룹_등록됨("신메뉴");
        return 메뉴_등록됨("강정치킨", BigDecimal.valueOf(15_000L), 신메뉴.getId(), 강정치킨);
    }

    public static MenuResponse 메뉴_등록됨(String name, BigDecimal price, Long menuGroupId,
                                      ProductResponse... products) {
        return 메뉴_생성_요청(name, price, menuGroupId, products).getBody();
    }

    static ResponseEntity<MenuResponse> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId,
                                                        ProductResponse... products) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("price", price);
        request.put("menuGroupId", menuGroupId);
        request.put("menuProducts", toMenuProducts(products));
        return restTemplate().postForEntity("/api/menus", request, MenuResponse.class);
    }

    private static List<MenuProductRequest> toMenuProducts(ProductResponse... products) {
        return Arrays.stream(products)
                     .map(p -> new MenuProductRequest(p.getId(), 1L)).collect(Collectors.toList());
    }

    static ResponseEntity<List<MenuResponse>> 메뉴_목록_조회_요청() {
        return restTemplate().exchange("/api/menus", HttpMethod.GET, null,
                                       new ParameterizedTypeReference<List<MenuResponse>>() {});
    }

    static void 메뉴_생성됨(ResponseEntity<MenuResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    static void 메뉴_생성_실패됨(ResponseEntity<MenuResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static void 메뉴_목록_응답됨(ResponseEntity<List<MenuResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    static void 메뉴_목록_확인됨(ResponseEntity<List<MenuResponse>> response, String... names) {
        List<String> productNames = response.getBody()
                                            .stream()
                                            .map(MenuResponse::getName)
                                            .collect(Collectors.toList());
        assertThat(productNames).containsExactly(names);
    }

    static void 메뉴_목록_메뉴에_메뉴_상품이_포함됨(ResponseEntity<List<MenuResponse>> response,
                                            ProductResponse... products) {
        List<Long> actualProductIds = response.getBody()
                                              .stream()
                                              .flatMap(menu -> menu.getMenuProducts().stream())
                                              .map(MenuProductResponse::getProductId)
                                              .collect(Collectors.toList());
        List<Long> expectedProductIds = Arrays.stream(products)
                                              .map(ProductResponse::getId)
                                              .collect(Collectors.toList());
        assertThat(actualProductIds).containsExactlyElementsOf(expectedProductIds);
    }
}
