package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_등록되어_있음;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_목록_조회_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_목록_조회됨;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_목록_포함됨;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_생성_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_생성됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest{
    private MenuGroup 애플;
    private MenuGroup 삼성;

    @BeforeEach
    void menuGroupSetUp() {
        super.setUp();

        애플 = new MenuGroup(null, "애플");
        삼성 = new MenuGroup(null, "삼성");
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(애플);

        메뉴_그룹_생성됨(response);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void getMenuGroupList() {
        ExtractableResponse<Response> createResponse1 = 메뉴_그룹_등록되어_있음(애플);
        ExtractableResponse<Response> createResponse2 = 메뉴_그룹_등록되어_있음(삼성);

        ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

        메뉴_그룹_목록_조회됨(response);
        메뉴_그룹_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }
}
