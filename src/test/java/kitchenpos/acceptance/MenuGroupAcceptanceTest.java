package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import kitchenpos.menus.menugroup.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 그룹 기능에 대한 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    private static final String MENU_GROUP_URL = "/api/menu-groups";

    @TestFactory
    Stream<DynamicTest> menuGroupTest() {
        return Stream.of(
                dynamicTest("메뉴 그룹을 생성 한다.", () -> {
                    ExtractableResponse<Response> 중식_생성_요청_응답 = 메뉴그룹_생성_요청("중식");
                    메뉴그룹_생성됨(중식_생성_요청_응답);
                }),
                dynamicTest("이름이 없는 메뉴그룹을 생성하면 실패한다.", () -> {
                    ExtractableResponse<Response> 생성_요청_응답 = 메뉴그룹_생성_요청("");
                    메뉴그룹_생성_실패됨(생성_요청_응답);
                }),
                dynamicTest("메뉴그룹 목록을 조회한다.", () -> {
                    ExtractableResponse<Response> 메뉴그룹_조회_요청_응답 = 메뉴그룹_조회_요청();
                    메뉴그룹_조회됨(메뉴그룹_조회_요청_응답);
                })
        );
    }

    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(String name) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(MenuGroupRequest.from(name))
                .when().post(MENU_GROUP_URL)
                .then().log().all().
                extract();
    }

    public static void 메뉴그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    public static void 메뉴그룹_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 메뉴그룹_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 메뉴그룹_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get(MENU_GROUP_URL)
                .then().log().all()
                .extract();
    }

}
