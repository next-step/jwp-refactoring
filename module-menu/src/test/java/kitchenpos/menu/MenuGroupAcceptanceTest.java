package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.ui.MenuGroupRestControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptancePerMethodTest {

    @DisplayName("메뉴그룹 등록")
    @Test
    void create() {
        // When
        ExtractableResponse<Response> 등록_응답 = 메뉴그룹_등록_요청("한식");
        // Then
        메뉴그룹이_등록됨(등록_응답);

        // When
        ExtractableResponse<Response> 목록_조회_응답 = 메뉴그룹_목록_조회_요청();
        // Then
        메뉴그룹_목록_조회됨(목록_조회_응답);
    }

    public static ExtractableResponse<Response> 메뉴그룹_목록_조회_요청() {
        return get(MenuGroupRestControllerTest.BASE_URL);
    }

    private List<MenuGroupResponse> 메뉴그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<MenuGroupResponse> 목록_조회_응답 = new ArrayList<>(response.jsonPath().getList(".", MenuGroupResponse.class));
        assertThat(목록_조회_응답).hasSize(1);
        return 목록_조회_응답;
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        return post(params, MenuGroupRestControllerTest.BASE_URL);
    }

    public static void 메뉴그룹이_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

}
