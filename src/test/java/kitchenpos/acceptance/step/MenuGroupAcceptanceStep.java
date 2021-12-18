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

    private static final String PRODUCT_API_URL = "/api/menu-groups";

    private MenuGroupAcceptanceStep() {
    }

    public static MenuGroup 메뉴그룹_등록됨(MenuGroup 등록요청_메뉴그룹) {
        ExtractableResponse<Response> 메뉴그룹_등록_결과 = 메뉴그룹_등록_요청(등록요청_메뉴그룹);

        return 메뉴그룹_등록됨(메뉴그룹_등록_결과, 등록요청_메뉴그룹);
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(MenuGroup 등록요청_메뉴그룹) {
        return RestAssured
            .given().log().all()
            .body(등록요청_메뉴그룹)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(PRODUCT_API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(PRODUCT_API_URL)
            .then().log().all()
            .extract();
    }

    public static MenuGroup 메뉴그룹_등록됨(ExtractableResponse<Response> 메뉴그룹_등록_결과, MenuGroup 예상_메뉴) {
        MenuGroup 등록된_메뉴그룹 = 메뉴그룹_등록_결과.as(MenuGroup.class);
        assertThat(등록된_메뉴그룹.getId()).isNotNull();
        assertThat(등록된_메뉴그룹.getName()).isEqualTo(예상_메뉴.getName());

        return 등록된_메뉴그룹;
    }

    public static List<MenuGroup> 메뉴그룹_목록조회_됨(ExtractableResponse<Response> 메뉴그룹_목록조회_결과,
        MenuGroup 등록된_메뉴그룹) {
        List<MenuGroup> 조회된_메뉴그룹_목록 = 메뉴그룹_목록조회_결과.as(new TypeRef<List<MenuGroup>>() {
        });
        assertThat(조회된_메뉴그룹_목록).contains(등록된_메뉴그룹);

        return 조회된_메뉴그룹_목록;
    }
}
