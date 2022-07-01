package kitchenpos.menugroup.acceptance;

import static kitchenpos.menugroup.acceptance.MenuGroupRestAssured.메뉴그룹_등록_요청;
import static kitchenpos.menugroup.acceptance.MenuGroupRestAssured.메뉴그룹_목록_조회_요청;
import static kitchenpos.utils.DomainFixtureFactory.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.utils.AcceptanceTest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴그룹 관련 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest {
    private MenuGroupRequest 두마리메뉴;
    private MenuGroupRequest 한마리메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        두마리메뉴 = createMenuGroupRequest("두마리메뉴");
        한마리메뉴 = createMenuGroupRequest("한마리메뉴");
    }

    /**
     * When 메뉴그룹을 등록 요청하면
     * Then 메뉴그룹이 등록 됨
     */
    @DisplayName("메뉴그룹을 등록한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_등록_요청(두마리메뉴);

        // then
        메뉴그룹_등록됨(response);
    }

    /**
     * Given 메뉴그룹을 등록하고
     * When 메뉴그룹 목록을 조회 하면
     * Then 메뉴그룹 목록 조회 됨
     */
    @DisplayName("메뉴그룹 목록을 조회 한다.")
    @Test
    void lists() {
        // given
        MenuGroupResponse 등록한_두마리메뉴 = 메뉴그룹_등록_요청(두마리메뉴).as(MenuGroupResponse.class);
        MenuGroupResponse 등록한_한마리메뉴 = 메뉴그룹_등록_요청(한마리메뉴).as(MenuGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 메뉴그룹_목록_조회_요청();

        // then
        메뉴그룹_목록_조회됨(response, Lists.newArrayList(등록한_두마리메뉴, 등록한_한마리메뉴));
    }

    private void 메뉴그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴그룹_목록_조회됨(ExtractableResponse<Response> response, List<MenuGroupResponse> expectedMenuGroups) {
        List<MenuGroupResponse> menuGroups = response.jsonPath().getList(".", MenuGroupResponse.class);
        assertThat(menuGroups).containsExactlyElementsOf(expectedMenuGroups);
    }
}
