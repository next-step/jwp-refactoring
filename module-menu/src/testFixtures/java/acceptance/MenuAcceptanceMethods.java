package acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static acceptance.RestAssuredMethods.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceMethods {
    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest params) {
        return post("/api/menus", params);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return get("/api/menus");
    }

    public static void 메뉴_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedMenuIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultMenuIds = response.jsonPath().getList(".", MenuResponse.class).stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuIds).containsAll(expectedMenuIds);
    }

    public static void 메뉴_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 메뉴_등록되어_있음(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return 메뉴_등록_요청(MenuRequest.of(name, price, menuGroupId, menuProducts));
    }
}
