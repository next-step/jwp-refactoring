package kitchenpos.menugroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static kitchenpos.menugroup.MenuGroupSteps.*;
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

    private void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", MenuGroupResponse.class).size()).isPositive();
    }
}
