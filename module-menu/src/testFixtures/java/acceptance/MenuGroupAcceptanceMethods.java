package acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static acceptance.RestAssuredMethods.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupAcceptanceMethods {
    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(MenuGroupRequest params) {
        return post("/api/menu-groups", params);
    }

    public static ExtractableResponse<Response> 메뉴그룹_목록_조회_요청() {
        return get("/api/menu-groups");
    }

    public static void 메뉴그룹_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴그룹_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴그룹_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedMenuGroupIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultMenuGroupIds = response.jsonPath().getList(".", MenuGroupResponse.class).stream()
                .map(MenuGroupResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuGroupIds).containsAll(expectedMenuGroupIds);
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록되어_있음(String name) {
        return 메뉴그룹_등록_요청(MenuGroupRequest.from(name));
    }
}
