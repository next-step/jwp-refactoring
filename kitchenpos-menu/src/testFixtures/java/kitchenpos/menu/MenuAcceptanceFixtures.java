package kitchenpos.menu;

import kitchenpos.common.CustomTestRestTemplate;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import kitchenpos.menugroup.MenuGroupAcceptanceFixtures;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import kitchenpos.product.ProductAcceptanceFixtures;

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

    public static MenuResponse 후라이드_앤드_양념_메뉴_생성() {
        MenuGroupResponse 추천메뉴 = MenuGroupAcceptanceFixtures.메뉴그룹_생성_요청(
            MenuGroupAcceptanceFixtures.메뉴그룹_정의("추천메뉴")).getBody();
        ProductResponse 양념치킨 = ProductAcceptanceFixtures.상품_생성_요청(
            ProductAcceptanceFixtures.상품_정의("양념치킨", BigDecimal.valueOf(10000))).getBody();
        ProductResponse 후라이드치킨 = ProductAcceptanceFixtures.상품_생성_요청(
            ProductAcceptanceFixtures.상품_정의("후라이드", BigDecimal.valueOf(9000))).getBody();

        MenuProductRequest 양념치킨_하나 = MenuAcceptanceFixtures.메뉴상품_정의(양념치킨.getId(), 1);
        MenuProductRequest 후라이드치킨_둘 = MenuAcceptanceFixtures.메뉴상품_정의(후라이드치킨.getId(), 2);
        return MenuAcceptanceFixtures.메뉴_생성_요청(
            MenuAcceptanceFixtures.메뉴_정의("후라이드와 양념", BigDecimal.valueOf(27000),
                추천메뉴.getId(), Arrays.asList(양념치킨_하나, 후라이드치킨_둘))).getBody();
    }

    public static MenuResponse 허니콤보_메뉴_생성() {
        MenuGroupResponse 베스트메뉴 = MenuGroupAcceptanceFixtures.메뉴그룹_생성_요청(
            MenuGroupAcceptanceFixtures.메뉴그룹_정의("베스트메뉴")).getBody();
        ProductResponse 허니다리 = ProductAcceptanceFixtures.상품_생성_요청(
            ProductAcceptanceFixtures.상품_정의("허니다리", BigDecimal.valueOf(13000))).getBody();
        ProductResponse 허니날개 = ProductAcceptanceFixtures.상품_생성_요청(
            ProductAcceptanceFixtures.상품_정의("허니날개", BigDecimal.valueOf(12000))).getBody();

        MenuProductRequest 허니다리_하나 = MenuAcceptanceFixtures.메뉴상품_정의(허니다리.getId(), 1);
        MenuProductRequest 허니날개_하나 = MenuAcceptanceFixtures.메뉴상품_정의(허니날개.getId(), 1);
        return MenuAcceptanceFixtures.메뉴_생성_요청(
            MenuAcceptanceFixtures.메뉴_정의("허니콤보", BigDecimal.valueOf(24000),
                베스트메뉴.getId(), Arrays.asList(허니다리_하나, 허니날개_하나))).getBody();
    }
}
