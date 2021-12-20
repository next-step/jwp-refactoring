package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.http.MediaType;

public class MenuAcceptanceStep {

    private static final String API_URL = "/api/menus";

    private MenuAcceptanceStep() {
    }

    public static Menu 메뉴_등록됨(Menu menu) {
        return 메뉴_등록_검증(메뉴_등록_요청(menu), menu);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(Menu menu) {
        return RestAssured
            .given().log().all()
            .body(menu)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(API_URL)
            .then().log().all()
            .extract();
    }

    public static Menu 메뉴_등록_검증(ExtractableResponse<Response> response, Menu expected) {
        Menu 등록된_메뉴 = response.as(Menu.class);
        assertThat(등록된_메뉴.getId()).isNotNull();
        assertThat(등록된_메뉴.getPrice()).isEqualByComparingTo(expected.getPrice());
        assertThat(등록된_메뉴.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());

        return 등록된_메뉴;
    }

    public static List<Menu> 메뉴_목록조회_검증(ExtractableResponse<Response> response, Menu expected) {
        List<Menu> 조회된_메뉴_목록 = response.as(new TypeRef<List<Menu>>() {
        });
        assertThat(조회된_메뉴_목록).contains(expected);

        return 조회된_메뉴_목록;
    }
}
