package kitchenpos.acceptance.step;


import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceStep {

    private static final String API_URL = "/api/menu-groups";

    private MenuGroupAcceptanceStep() {
    }

    public static MenuGroup 메뉴그룹_등록됨(MenuGroup menuGroup) {
        ExtractableResponse<Response> 메뉴그룹_등록_결과 = 메뉴그룹_등록_요청(menuGroup);

        return 메뉴그룹_등록됨(메뉴그룹_등록_결과, menuGroup);
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(MenuGroup menuGroup) {
        return RestAssured
            .given().log().all()
            .body(menuGroup)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(API_URL)
            .then().log().all()
            .extract();
    }

    public static MenuGroup 메뉴그룹_등록됨(ExtractableResponse<Response> response, MenuGroup expected) {
        MenuGroup 등록된_메뉴그룹 = response.as(MenuGroup.class);
        assertThat(등록된_메뉴그룹.getId()).isNotNull();
        assertThat(등록된_메뉴그룹.getName()).isEqualTo(expected.getName());

        return 등록된_메뉴그룹;
    }

    public static List<MenuGroup> 메뉴그룹_목록조회_됨(ExtractableResponse<Response> response,
        MenuGroup expected) {
        List<MenuGroup> 조회된_메뉴그룹_목록 = response.as(new TypeRef<List<MenuGroup>>() {
        });
        assertThat(조회된_메뉴그룹_목록).contains(expected);

        return 조회된_메뉴그룹_목록;
    }
}
