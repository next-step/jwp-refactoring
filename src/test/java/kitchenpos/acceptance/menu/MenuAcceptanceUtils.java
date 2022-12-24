package kitchenpos.acceptance.menu;

import static kitchenpos.acceptance.menugroup.MenuGroupAcceptanceUtils.*;
import static kitchenpos.acceptance.product.ProductAcceptanceUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.menugroup.MenuGroupId;
import kitchenpos.acceptance.product.ProductId;

public class MenuAcceptanceUtils {
    private MenuAcceptanceUtils() {}

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name, Long price, MenuGroupId menuGroupId,
        List<MenuProductParam> menuProductParams) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("price", price);
        body.put("menuGroupId", menuGroupId.value());
        body.put("menuProducts", menuProductParams);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/api/menus")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_등록_요청_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menus")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_목록_조회_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static List<String> 메뉴_목록_이름_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

    public static List<ProductId> 메뉴_목록_상품_ID_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("[0].menuProducts.productId", Integer.class).stream()
            .map(ProductId::new)
            .collect(Collectors.toList());
    }

    public static void 메뉴_등록_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static MenuId 메뉴_ID_추출(ExtractableResponse<Response> response) {
        return new MenuId(response.jsonPath().getLong("id"));
    }

    public static MenuId 메뉴_등록되어_있음() {
        MenuGroupId 메뉴_그룹_ID = 메뉴_그룹_ID_추출(메뉴_그룹_등록_요청("치킨 그룹"));
        ProductId 상품_ID = 상품_ID_추출(상품_등록_요청("후라이드", 10000));
        List<MenuProductParam> 메뉴_상품_목록 = Arrays.asList(new MenuProductParam(상품_ID, 2));
        return 메뉴_ID_추출(메뉴_등록_요청("후라이드+후라이드", 19000L, 메뉴_그룹_ID, 메뉴_상품_목록));
    }
}
