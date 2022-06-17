package kitchenpos.domain;

import static kitchenpos.domain.MenuGroupAcceptanceTestMethod.메뉴_그룹_등록_요청;
import static kitchenpos.domain.MenuGroupAcceptanceTestMethod.메뉴_그룹_등록되어_있음;
import static kitchenpos.domain.MenuGroupAcceptanceTestMethod.메뉴_그룹_등록됨;
import static kitchenpos.domain.MenuGroupAcceptanceTestMethod.메뉴_그룹_목록_응답됨;
import static kitchenpos.domain.MenuGroupAcceptanceTestMethod.메뉴_그룹_목록_조회_요청;
import static kitchenpos.domain.MenuGroupAcceptanceTestMethod.메뉴_그룹_목록_포함됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴그룹 관련 기능 인수테스트")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    private MenuGroup 분식_메뉴그룹;
    private MenuGroup 초밥_메뉴그룹;

    @BeforeEach
    public void setUp() {
        super.setUp();

        분식_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "분식 메뉴그룹");
        초밥_메뉴그룹 = MenuGroupFixtureFactory.create(2L, "초밥 메뉴그룹");
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create01() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_등록_요청(초밥_메뉴그룹);

        // then
        메뉴_그룹_등록됨(response);
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        ExtractableResponse<Response> createdResponse1 = 메뉴_그룹_등록되어_있음(분식_메뉴그룹);
        ExtractableResponse<Response> createdResponse2 = 메뉴_그룹_등록되어_있음(초밥_메뉴그룹);

        // when
        ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_목록_응답됨(response);
        메뉴_그룹_목록_포함됨(response, Lists.newArrayList(createdResponse1, createdResponse2));
    }
}