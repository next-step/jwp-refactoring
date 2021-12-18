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

    private static final String PRODUCT_API_URL = "/api/menus";

    private MenuAcceptanceStep() {
    }

    public static Menu 메뉴_등록됨(Menu 등록_파라미터) {
        return 메뉴_등록_검증(메뉴_등록_요청(등록_파라미터), 등록_파라미터);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(Menu 등록_파라미터) {
        return RestAssured
            .given().log().all()
            .body(등록_파라미터)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(PRODUCT_API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(PRODUCT_API_URL)
            .then().log().all()
            .extract();
    }

    public static Menu 메뉴_등록_검증(ExtractableResponse<Response> 메뉴_등록_결과, Menu 예상_메뉴) {
        Menu 등록된_메뉴 = 메뉴_등록_결과.as(Menu.class);
        assertThat(등록된_메뉴.getId()).isNotNull();
        assertThat(등록된_메뉴.getPrice()).isEqualByComparingTo(예상_메뉴.getPrice());
        assertThat(등록된_메뉴.getMenuGroupId()).isEqualTo(예상_메뉴.getMenuGroupId());

        return 등록된_메뉴;
    }

    public static List<Menu> 메뉴_목록조회_검증(ExtractableResponse<Response> 메뉴_목록조회_결과, Menu 등록된_메뉴) {
        List<Menu> 조회된_메뉴_목록 = 메뉴_목록조회_결과.as(new TypeRef<List<Menu>>() {
        });
        assertThat(조회된_메뉴_목록).contains(등록된_메뉴);

        return 조회된_메뉴_목록;
    }
}
