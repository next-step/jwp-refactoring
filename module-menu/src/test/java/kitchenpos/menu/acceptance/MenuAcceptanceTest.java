package kitchenpos.menu.acceptance;

import static kitchenpos.menu.MenuTestFixture.감자튀김_FIXTURE;
import static kitchenpos.menu.MenuTestFixture.후라이드_치킨_FIXTURE;
import static kitchenpos.menu.acceptance.MenuAcceptanceSupport.메뉴_그룹_등록요청;
import static kitchenpos.menu.acceptance.MenuAcceptanceSupport.메뉴_정상_등록됨;
import static kitchenpos.menu.acceptance.MenuAcceptanceSupport.메뉴등록을_요청;
import static kitchenpos.menu.acceptance.MenuAcceptanceSupport.메뉴목록_정상_조회됨;
import static kitchenpos.menu.acceptance.MenuAcceptanceSupport.모든메뉴_조회요청;
import static kitchenpos.menu.acceptance.MenuAcceptanceSupport.상품_등록요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.acceptance.utils.AcceptanceTest;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.request.MenuGroupRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.product.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴에 관한 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    private static MenuRequest 치킨_메뉴;

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
        메뉴목록_정상_조회됨(getResponse, 1);
    }

    public static MenuRequest 치킨세트_메뉴_가져오기() {
        ProductResponse 후라이드_치킨 = 상품_등록요청(후라이드_치킨_FIXTURE).as(ProductResponse.class);
        ProductResponse 감자튀김 = 상품_등록요청(감자튀김_FIXTURE).as(ProductResponse.class);

        MenuProductRequest 메뉴_상품_후라이드_치킨 = new MenuProductRequest(후라이드_치킨.getId(), 1);
        MenuProductRequest 메뉴_상품_감자튀김 = new MenuProductRequest(감자튀김.getId(), 1);
        MenuGroupResponse 치킨_메뉴_그룹 = 메뉴_그룹_등록요청(new MenuGroupRequest("치킨_메뉴")).as(MenuGroupResponse.class);

        MenuRequest 치킨_메뉴 = new MenuRequest("후라이드치킨 세트", BigDecimal.valueOf(18000L), 치킨_메뉴_그룹.getId(),
            Arrays.asList(메뉴_상품_후라이드_치킨, 메뉴_상품_감자튀김));

        return 치킨_메뉴;
    }
}
