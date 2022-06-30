package kitchenpos.helper.AcceptanceApiHelper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.helper.testDTO.request.MenuGroupRequest;
import org.springframework.http.MediaType;

public class MenuGroupApiHelper {

    public static ExtractableResponse<Response> 메뉴그룹_등록하기(String 메뉴그룹_이름) {
        MenuGroupRequest menuGroup = new MenuGroupRequest();
        menuGroup.setName(메뉴그룹_이름);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuGroup)
            .when().post("/api/menu-groups")
            .then().log().all().
            extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_리스트_조회하기() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menu-groups")
            .then().log().all().
            extract();
    }

}
