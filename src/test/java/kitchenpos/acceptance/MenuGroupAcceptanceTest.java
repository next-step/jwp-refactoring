package kitchenpos.acceptance;

import static kitchenpos.utils.StatusValidation.생성됨;
import static kitchenpos.utils.StatusValidation.조회됨;
import static kitchenpos.utils.TestFactory.get;
import static kitchenpos.utils.TestFactory.post;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    public static final String MENU_GROUP_DEFAULT_URL = "/menu-groups";
    private MenuGroupRequest 세트메뉴;
    private MenuGroupRequest 일반메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();
        세트메뉴 = new MenuGroupRequest(5L, "세트메뉴");
        일반메뉴 = new MenuGroupRequest(6L, "일반메뉴");
    }

    @Test
    void 메뉴그룹을_생성한다() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_생성_요청(세트메뉴);

        // then
        메뉴그룹_생성됨(response);
    }

    @Test
    void 메뉴그룹_조회한다() {
        // given
        메뉴그룹_생성_요청(세트메뉴);
        메뉴그룹_생성_요청(일반메뉴);

        // when
        ExtractableResponse<Response> response = 메뉴그룹_전체조회_요청();

        // then
        메뉴그룹_전체조회됨(response);
    }


    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        return post(MENU_GROUP_DEFAULT_URL, menuGroupRequest);
    }

    public static void 메뉴그룹_생성됨(ExtractableResponse<Response> response) {
        생성됨(response);
    }

    public static ExtractableResponse<Response> 메뉴그룹_전체조회_요청() {
        return get(MENU_GROUP_DEFAULT_URL);
    }

    public static void 메뉴그룹_전체조회됨(ExtractableResponse<Response> response) {
        조회됨(response);
    }
}
