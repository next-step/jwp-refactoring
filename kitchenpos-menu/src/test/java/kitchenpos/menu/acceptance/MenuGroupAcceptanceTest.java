package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupTestFixture.메뉴_그룹_생성_요청함;
import static kitchenpos.menu.acceptance.MenuGroupTestFixture.메뉴_그룹_생성됨;
import static kitchenpos.menu.acceptance.MenuGroupTestFixture.메뉴_그룹_조회_요청_응답됨;
import static kitchenpos.menu.acceptance.MenuGroupTestFixture.메뉴_그룹_조회_요청함;
import static kitchenpos.menu.acceptance.MenuGroupTestFixture.메뉴_그룹_조회_포함됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    private String 추천메뉴;
    private String 오늘의메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();
        추천메뉴 = "추천메뉴";
        오늘의메뉴 = "오늘의메뉴";
    }

    @DisplayName("메뉴 그룹 등록한다.")
    @Test
    void create() {
        //when
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청함(추천메뉴);

        //then
        메뉴_그룹_생성됨(response);
    }

    @DisplayName("메뉴 그룹 조회한다.")
    @Test
    void list() {
        //given
        ExtractableResponse<Response> 추천메뉴_response = 메뉴_그룹_생성_요청함(추천메뉴);
        ExtractableResponse<Response> 오늘의메뉴_response = 메뉴_그룹_생성_요청함(오늘의메뉴);

        //when
        ExtractableResponse<Response> response = 메뉴_그룹_조회_요청함();

        //then
        메뉴_그룹_조회_요청_응답됨(response);
        메뉴_그룹_조회_포함됨(response, Arrays.asList(추천메뉴_response, 오늘의메뉴_response));

    }
}
