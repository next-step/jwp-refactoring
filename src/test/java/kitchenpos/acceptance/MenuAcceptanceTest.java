package kitchenpos.acceptance;

import static kitchenpos.acceptance.support.MenuAcceptanceSupport.메뉴_정상_등록됨;
import static kitchenpos.acceptance.support.MenuAcceptanceSupport.메뉴등록을_요청;
import static kitchenpos.acceptance.support.MenuAcceptanceSupport.메뉴목록_정상_조회됨;
import static kitchenpos.acceptance.support.MenuAcceptanceSupport.모든메뉴_조회요청;
import static kitchenpos.acceptance.support.MenuGroupAcceptanceSupport.메뉴_그룹_등록요청;
import static kitchenpos.acceptance.support.ProductAcceptanceSupport.상품_등록요청;
import static kitchenpos.acceptance.support.TestFixture.감자튀김_FIXTURE;
import static kitchenpos.acceptance.support.TestFixture.후라이드_치킨_FIXTURE;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴에 관한 인수 테스트")
class MenuAcceptanceTest extends AcceptanceTest {
    private static Menu 치킨_메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        치킨_메뉴 = 치킨세트_메뉴_가져오기();
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 메뉴등록을_요청(치킨_메뉴);

        // then
        메뉴_정상_등록됨(response);
    }

    @DisplayName("모든 메뉴를 조회한다")
    @Test
    void find_test() {
        // given
        메뉴등록을_요청(치킨_메뉴);

        // when
        ExtractableResponse<Response> getResponse = 모든메뉴_조회요청();

        // then
        메뉴목록_정상_조회됨(getResponse);
    }

    public static Menu 치킨세트_메뉴_등록함() {
        Menu 치킨_메뉴 = 치킨세트_메뉴_가져오기();
        ExtractableResponse<Response> response = 메뉴등록을_요청(치킨_메뉴);

        return response.as(Menu.class);
    }

    public static Menu 치킨세트_메뉴_가져오기() {
        Product 후라이드_치킨 = 상품_등록요청(후라이드_치킨_FIXTURE).as(Product.class);
        Product 감자튀김 = 상품_등록요청(감자튀김_FIXTURE).as(Product.class);

        MenuProduct 메뉴_상품_후라이드_치킨 = MenuProduct.of(null, null, 후라이드_치킨.getId(), 1);
        MenuProduct 메뉴_상품_감자튀김 = MenuProduct.of(null, null, 감자튀김.getId(), 1);
        MenuGroup 치킨_메뉴_그룹 = 메뉴_그룹_등록요청(MenuGroup.of(null, "치킨_메뉴")).as(MenuGroup.class);

        Menu 치킨_메뉴 = Menu.of(null, "후라이드치킨 세트", BigDecimal.valueOf(18000L), 치킨_메뉴_그룹.getId(),
            Arrays.asList(메뉴_상품_후라이드_치킨, 메뉴_상품_감자튀김));

        return 치킨_메뉴;
    }
}
