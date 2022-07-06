package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceTest extends BaseAcceptanceTest {

    @DisplayName("메뉴그룹을 관리한다")
    @Test
    public void manageMenuGroup() {
        //메뉴 그룹 생성
        //given
        String 메뉴명 = "1번메뉴";
        //when
        ExtractableResponse<Response> 메뉴_그룹_생성_요청 = 메뉴_그룹_생성_요청(메뉴명);
        //then
        메뉴_그룹_생성됨(메뉴_그룹_생성_요청);

        //메뉴 그룹 조회
        //when

        ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청 = 메뉴_그룹_목록_조회_요청();
        //then
        메뉴_그룹_조회됨(메뉴_그룹_목록_조회_요청, 메뉴명);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
        final Map<String, Object> content = new HashMap<>();
        content.put("name", name);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(content)
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

    public static void 메뉴_그룹_조회됨(final ExtractableResponse<Response> response, String name) {
        List<String> menuNames = response.jsonPath().getList(".", MenuGroupResponse.class).stream().map(menuGroup -> menuGroup.getName()).collect(Collectors.toList());

        assertThat(menuNames).contains(name);
    }

}
