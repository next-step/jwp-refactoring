package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.common.acceptance.BaseAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceTest extends BaseAcceptanceTest {
    @DisplayName("메뉴 그룹을 관리한다")
    @Test
    void manageMenuGroup() {
        // given
        final String newMenuGroupName1 = "구이류";
        final String newMenuGroupName2 = "식사류";

        // when
        ExtractableResponse<Response> created1 = 메뉴_그룹_생성_요청(newMenuGroupName1);
        // then
        메뉴_그룹_생성됨(created1);

        // when
        ExtractableResponse<Response> created2 = 메뉴_그룹_생성_요청(newMenuGroupName2);
        // then
        메뉴_그룹_생성됨(created2);


        // when
        ExtractableResponse<Response> list = 메뉴_그룹_목록_조회_요청();
        // then
        메뉴_그룹_목록_조회됨(list);
        // then
        메뉴_그룹_목록_포함됨(list, Arrays.asList(newMenuGroupName1, newMenuGroupName2));
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(final String name) {
        final Map<String, Object> body = new HashMap<>();
        body.put("name", name);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_그룹_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_그룹_목록_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_그룹_목록_포함됨(final ExtractableResponse<Response> response, final List<String> menuGroupNames) {
        assertThat(response.body().asString()).contains(menuGroupNames);
    }
}
