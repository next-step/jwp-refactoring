package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 그룸 관련 인수테스트")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    private static final String MENU_GROUP_PATH = "/api/menu-groups";

    /**
     * Feature: 메뉴 그룹 기능
     *
     *   Scenario: 메뉴 그룹을 관리
     *     Given  메뉴 그룹 명으로
     *     When   메뉴 그룸 등록 요청하면
     *     Then   메뉴 그룹이 등록된다.
     *
     *     When   메뉴 그룹 목록을 조회하면
     *     Then   메뉴 그룹 목록이 조회된다.
     * */
    @DisplayName("메뉴 그룹을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageMenuGroup() {
        return Stream.of(
                dynamicTest("메뉴 그룹을 등록 한다.", () -> {
                    //given
                    Map<String, Object> params = new HashMap<>();
                    params.put("name", "인기 메뉴");

                    //when
                    ExtractableResponse<Response> response = 메뉴_그룹_등록_요청(params);

                    //then
                    메뉴_그룹_등록됨(response);
                }),

                dynamicTest("메뉴 그룹 목록 조회한다.", () -> {
                    //when
                    ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

                    //then
                    메뉴_그룹_목록_조회됨(response);
                })
        );

    }

    private ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(MENU_GROUP_PATH)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴_그룹_등록_요청(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(MENU_GROUP_PATH)
                .then().log().all()
                .extract();
    }

    private void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("name", String.class)).contains("인기 메뉴");
    }

}
