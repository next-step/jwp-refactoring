package kitchenpos.Acceptance.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.AcceptanceTest;
import kitchenpos.Acceptance.utils.RestAssuredRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static kitchenpos.Acceptance.menuGroup.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.Acceptance.product.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.domain.MenuProductTest.메뉴_상품_생성;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/api/menus";
    private static final int PRODUCT_PRICE = 1_000;

    private MenuGroup menuGroup;
    private Product product;

    @BeforeEach
    public void setUp() {
        super.setUp();
        menuGroup = 메뉴_그룹_생성_요청("메뉴 그룹").as(MenuGroup.class);
        product = 상품_생성_요청("상품", PRODUCT_PRICE).as(Product.class);
    }

    @DisplayName("메뉴의 가격이 0원 미만인 경우 생성 요청이 실패해야 한다")
    @Test
    void createMenuByMinusPriceTest() {
        // when
        ExtractableResponse<Response> 메뉴_생성_결과 = 메뉴_생성_요청("메뉴", -1, menuGroup.getId(), Collections.emptyList());

        // then
        메뉴_생성_실패됨(메뉴_생성_결과);
    }

    @DisplayName("메뉴 생성 시 메뉴 그룹 정보가 없거나 저장되지 않은 메뉴 그룹으로 생성 요청이 실패해야 한다")
    @Test
    void createMenuByNotSavedMenuGroupTest() {
        // when
        ExtractableResponse<Response> 없는_메뉴_그룹으로_메뉴_생성_결과
                = 메뉴_생성_요청("메뉴", 1_000, -1L, Collections.emptyList());
        ExtractableResponse<Response> 메뉴_그룹_정보_없이_메뉴_생성_결과
                = 메뉴_생성_요청("메뉴", 1_000, null, Collections.emptyList());

        // then
        메뉴_생성_실패됨(없는_메뉴_그룹으로_메뉴_생성_결과);
        메뉴_생성_실패됨(메뉴_그룹_정보_없이_메뉴_생성_결과);
    }

    @DisplayName("없는 상품으로 메뉴를 생성하면 생성 요청이 실패해야 한다")
    @Test
    void createMenuByNotSavedProductTest() {
        // given
        MenuProduct 상품_정보가_없는_메뉴_상품 = 메뉴_상품_생성(null, null, 1);
        MenuProduct 없는_상품이_포함된_메뉴_상품 = 메뉴_상품_생성(null, -1L, 1);

        // when
        ExtractableResponse<Response> 상품_정보가_없는_메뉴_상품_메뉴_생성_결과 =
                메뉴_생성_요청("메뉴", 1_000, menuGroup.getId(), Collections.singletonList(상품_정보가_없는_메뉴_상품));
        ExtractableResponse<Response> 없는_상품이_포함된_메뉴_상품_메뉴_생성_결과 =
                메뉴_생성_요청("메뉴", 1_000, menuGroup.getId(), Collections.singletonList(없는_상품이_포함된_메뉴_상품));

        // then
        메뉴_생성_실패됨(상품_정보가_없는_메뉴_상품_메뉴_생성_결과);
        메뉴_생성_실패됨(없는_상품이_포함된_메뉴_상품_메뉴_생성_결과);
    }

    @DisplayName("메뉴에 포함된 상품의 총 가격과 메뉴의 가격이 다른 메뉴를 생성 요청하면 실패해야 한다")
    @Test
    void createMenuByNotMatchedMenuPriceAndProductTotalPriceTest() {
        // given
        int menuProductQuantity = 2;
        MenuProduct 메뉴_상품 = 메뉴_상품_생성(null, product.getId(), menuProductQuantity);

        // when
        ExtractableResponse<Response> 메뉴_생성_결과 =
                메뉴_생성_요청("메뉴", 3_000, menuGroup.getId(), Collections.singletonList(메뉴_상품));

        // then
        메뉴_생성_실패됨(메뉴_생성_결과);
    }

    @DisplayName("정상 상태의 메뉴를 저장하면 메뉴 생성이 성공되어야 한다")
    @Test
    void createMenuTest() {
        // given
        String 메뉴_이름 = "메뉴";
        int menuProductQuantity = 2;
        MenuProduct 메뉴_상품 = 메뉴_상품_생성(null, product.getId(), menuProductQuantity);

        // when
        ExtractableResponse<Response> 메뉴_생성_결과 =
                메뉴_생성_요청("메뉴", PRODUCT_PRICE * menuProductQuantity, menuGroup.getId(), Collections.singletonList(메뉴_상품));

        // then
        메뉴_생성_성공됨(메뉴_생성_결과, 메뉴_이름, PRODUCT_PRICE * menuProductQuantity);
    }

    @DisplayName("메뉴 리스트 조회 시 정상 조회되어야 한다")
    @Test
    void findAllMenuTest() {
        // given
        List<String> 메뉴_이름들 = Arrays.asList("메뉴 1", "메뉴 2", "메뉴 3", "메뉴 4", "메뉴 5");
        MenuProduct 메뉴_상품 = 메뉴_상품_생성(null, product.getId(), PRODUCT_PRICE);
        메뉴_이름들.forEach(name -> 메뉴_생성_요청(name, PRODUCT_PRICE, menuGroup.getId(), Collections.singletonList(메뉴_상품)));

        // when
        ExtractableResponse<Response> 상품_생성_결과 = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회_성공됨(상품_생성_결과, 메뉴_이름들);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(
            String name, int menuPrice, Long menuGroupId, List<MenuProduct> menuProducts
    ) {
        Map<String, Object> body = new HashMap<>();

        body.put("name", name);
        body.put("price", menuPrice);
        body.put("menuGroupId", menuGroupId);
        body.put("menuProducts", menuProducts);

        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), body);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssuredRequest.getRequest(PATH, Collections.emptyMap());
    }

    void 메뉴_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    void 메뉴_생성_성공됨(ExtractableResponse<Response> response, String expectedMenuName, int expectedMenuPrice) {
        Menu menu = response.as(Menu.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(menu.getName()).isEqualTo(expectedMenuName);
        assertThat(menu.getPrice().compareTo(new BigDecimal(expectedMenuPrice))).isEqualTo(0);
    }

    void 메뉴_목록_조회_성공됨(ExtractableResponse<Response> response, List<String> expectedMenuNames) {
        List<String> names = response.body().jsonPath().getList("name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(names).containsAll(expectedMenuNames);
    }
}
