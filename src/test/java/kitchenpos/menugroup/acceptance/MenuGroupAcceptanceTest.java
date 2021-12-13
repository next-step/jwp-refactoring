package kitchenpos.menugroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

@DisplayName("메뉴 그룹 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴 그룹을 관리한다.")
    void create() {
        // given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("한마리메뉴");

        // when
        ExtractableResponse<Response> createResponse = 메뉴_그룹_등록_요청(menuGroupRequest);

        // then
        메뉴_그룹_등록됨(createResponse);

        // when
        ExtractableResponse<Response> listResponse = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_목록_조회됨(listResponse);
    }

    public static MenuGroupResponse 메뉴_그룹_등록되어_있음(MenuGroupRequest menuGroupRequest) {
        return 메뉴_그룹_등록_요청(menuGroupRequest).as(MenuGroupResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroupRequest menuGroupRequest) {
        return post("/api/menu-groups", menuGroupRequest);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return get("/api/menu-groups");
    }

    private void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", MenuGroup.class).size()).isPositive();
    }
}
