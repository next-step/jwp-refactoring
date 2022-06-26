package kitchenpos.helper.AcceptanceApiHelper;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.dto.MenuProductDTO;
import kitchenpos.dto.request.MenuRequest;
import org.springframework.http.MediaType;

public class MenuApiHelper {

    public static ExtractableResponse<Response> 메뉴_추가하기(String 메뉴이름, int 메뉴가격, long 메뉴그룹_아이디, List<MenuProductDTO> 메뉴리스트) {
        MenuRequest 메뉴정보 = new MenuRequest();
        메뉴정보.setName(메뉴이름);
        메뉴정보.setPrice(BigDecimal.valueOf(메뉴가격));
        메뉴정보.setMenuGroupId(메뉴그룹_아이디);
        메뉴정보.setMenuProducts(메뉴리스트);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(메뉴정보)
            .when().post("/api/menus")
            .then().log().all().
            extract();
    }

    public static ExtractableResponse<Response> 메뉴_리스트_조회하기() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menus")
            .then().log().all().
            extract();
    }

}
