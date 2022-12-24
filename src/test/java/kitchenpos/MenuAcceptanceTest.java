package kitchenpos;

import static kitchenpos.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private static MenuGroup 피자세트;
    private static Product 페페로니피자;
    private static Product 콜라;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        피자세트 = 메뉴_그룹_생성_요청("피자세트").as(MenuGroup.class);
        페페로니피자 = 상품_생성_요청("페페로니피자", BigDecimal.valueOf(20_000L)).as(Product.class);
        콜라 = 상품_생성_요청("콜라", BigDecimal.valueOf(2_000L)).as(Product.class);
    }

    /**
     * Feature: 메뉴 관련 기능
     *  Background:
     *      given: 메뉴 그룹을 생성한다
     *      and: 상품을 생성한다
     * <p>
     *  Scenario: 상품 추가 조회
     *      when: 메뉴를 생성한다
     *      then: 메뉴가 생성됨
     * <p>
     *      when: 이름이 없는 메뉴를생성한다
     *      then: 메뉴 생성이 실패됨
     *      when: 가격이 없는 메뉴를생성한다
     *      then: 메뉴 생성이 실패됨
     *      when: 가격이 음수인 메뉴를생성한다
     *      then: 메뉴 생성이 실패됨
     *      when: 메뉴 그룹 없이 메뉴를 생성한다
     *      then: 메뉴 생성이 실패됨
     *      when: 존재하지 않는 상품으로 메뉴를 생성한다
     *      then: 메뉴 생성이 실패됨
     *      when: 상품 가격보다 비싼 메뉴를 등록한다
     *      then: 메뉴 생성이 실패됨
     * <p>
     *      then: 메뉴 목록을 조회한다
     *      when: 메뉴 목록이 조회된다
     */

    @DisplayName("메뉴 추가 조회")
    @Test
    void scenarioMenu() {
        // when
        ExtractableResponse<Response> 생성 = 메뉴_생성_요청("페페로니피자, 콜라",
                BigDecimal.valueOf(15_000L), 피자세트.getId(), 페페로니피자, 콜라);
        // then
        메뉴_생성됨(생성);

        // when
        ExtractableResponse<Response> 이름없음 = 메뉴_생성_요청(null,
                BigDecimal.valueOf(15_000L), 피자세트.getId(), 페페로니피자, 콜라);
        // then
        메뉴_생성_실패됨(이름없음);

        // when
        ExtractableResponse<Response> 가격없음 = 메뉴_생성_요청("페페로니피자, 콜라",
                null, 피자세트.getId(), 페페로니피자, 콜라);
        // then
        메뉴_생성_실패됨(가격없음);

        // when
        ExtractableResponse<Response> 음수가격 = 메뉴_생성_요청("페페로니피자, 콜라",
                BigDecimal.valueOf(-1), 피자세트.getId(), 페페로니피자, 콜라);
        // then
        메뉴_생성_실패됨(음수가격);

        // when
        ExtractableResponse<Response> 그룹없음 = 메뉴_생성_요청("페페로니피자, 콜라",
                BigDecimal.valueOf(15_000L), null, 페페로니피자, 콜라);
        // then
        메뉴_생성_실패됨(그룹없음);

        //given
        Product 존재하지_않는_상품 = new Product();
        // when
        ExtractableResponse<Response> 없는상품 = 메뉴_생성_요청("페페로니피자, 콜라",
                BigDecimal.valueOf(15_000L), 피자세트.getId(), 존재하지_않는_상품);
        // then
        메뉴_생성_실패됨(없는상품);

        // when
        ExtractableResponse<Response> 비싼메뉴 = 메뉴_생성_요청("페페로니피자, 콜라",
                BigDecimal.valueOf(30_000L), 피자세트.getId(), 페페로니피자, 콜라);
        // then
        메뉴_생성_실패됨(비싼메뉴);

        // when
        ExtractableResponse<Response> 조회 = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_확인됨(조회, "페페로니피자, 콜라");
        메뉴_목록_메뉴에_메뉴_상품이_포함됨(조회, 페페로니피자, 콜라);

    }


    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId,
                                                         Product... products) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("price", price);
        request.put("menuGroupId", menuGroupId);
        request.put("menuProducts", toMenuProducts(products));
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    private static List<MenuProduct> toMenuProducts(Product... products) {
        return Arrays.stream(products)
                .map(p -> {
                    MenuProduct menuProduct = new MenuProduct();
                    menuProduct.setProductId(p.getId());
                    menuProduct.setQuantity(1L);
                    return menuProduct;
                }).collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 메뉴_목록_확인됨(ExtractableResponse<Response> response, String... names) {
        List<Menu> menus = response.jsonPath().getList(".", Menu.class);

        List<String> productNames = menus.stream()
                .map(Menu::getName)
                .collect(Collectors.toList());
        assertThat(productNames).containsExactly(names);
    }

    public static void 메뉴_목록_메뉴에_메뉴_상품이_포함됨(ExtractableResponse<Response> response, Product... products) {
        List<Menu> menus = response.jsonPath().getList(".", Menu.class);

        List<Long> actualProductIds = menus.stream()
                .flatMap(menu -> menu.getMenuProducts().stream())
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());

        List<Long> expectedProductIds = Arrays.stream(products)
                .map(Product::getId)
                .collect(Collectors.toList());

        assertThat(actualProductIds).containsExactlyElementsOf(expectedProductIds);
    }
}
