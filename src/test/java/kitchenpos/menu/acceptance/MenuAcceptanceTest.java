package kitchenpos.menu.acceptance;

import static java.util.Arrays.asList;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.utils.StatusValidation.생성됨;
import static kitchenpos.utils.StatusValidation.조회됨;
import static kitchenpos.utils.TestFactory.get;
import static kitchenpos.utils.TestFactory.post;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MenuAcceptanceTest extends AcceptanceTest {

    public static final String MENU_DEFAULT_URL = "/menus";
    private MenuProductRequest 양념치킨_1개;
    private MenuProductRequest 코카콜라_1개;
    private MenuRequest 메뉴_양념치킨_콜라_세트;



    @BeforeEach
    public void setUp() {
        super.setUp();
        상품_생성_요청(new ProductRequest(1L, "양념치킨", 16000L));
        상품_생성_요청(new ProductRequest(2L, "코카콜라", 2000L));

        메뉴그룹_생성_요청(new MenuGroupRequest("세트메뉴")); ;
        메뉴그룹_생성_요청(new MenuGroupRequest("후라이드 그룹"));
        

        양념치킨_1개 = new MenuProductRequest(1L, 1L);
        코카콜라_1개 = new MenuProductRequest(2L, 1L);
        메뉴_양념치킨_콜라_세트 = new MenuRequest("양념치킨_콜라세트", 18000L, 1L,
            asList(양념치킨_1개, 코카콜라_1개));
    }

    @Test
    void 메뉴를_생성한다() {

        // when
        ExtractableResponse<Response> response = 메뉴_생성을_요청(메뉴_양념치킨_콜라_세트);

        // then
        메뉴_생성됨(response);
    }

    @Test
    void 메뉴를_조회한다() {

        // given
        메뉴_생성을_요청(메뉴_양념치킨_콜라_세트);

        // when
        ExtractableResponse<Response> response = 메뉴_전체_조회_요청();

        // then
        메뉴_조회됨(response);
    }

    public static ExtractableResponse<Response> 메뉴_생성을_요청(MenuRequest menuRequest) {
        return post(MENU_DEFAULT_URL, menuRequest);
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        생성됨(response);
    }

    public static ExtractableResponse<Response> 메뉴_전체_조회_요청() {
        return get(MENU_DEFAULT_URL);
    }

    public static void 메뉴_조회됨(ExtractableResponse<Response> response) {
        조회됨(response);
    }

}
