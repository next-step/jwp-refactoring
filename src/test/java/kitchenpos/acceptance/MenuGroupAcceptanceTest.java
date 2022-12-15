package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptance.MenuGroupAcceptanceStep.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 그룹 관련 인수 테스트")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    /**
     * When 메뉴 그룹 생성 요청
     * Then 메뉴 그룹 생성됨
     */
    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(MenuGroupRequest.from("프리미엄 치킨"));

        메뉴_그룹_생성됨(response);
    }

    /**
     * Given 메뉴 그룹 여러개 등록되어 있음
     * When 메뉴 그룹 목록 조회 요청
     * Then 메뉴 그룹 목록 조회됨
     * Then 메뉴 그룹 목록에 등록된 메뉴 그룹 포함됨
     */
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        ExtractableResponse<Response> createResponse1 = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from("프리미엄 치킨"));
        ExtractableResponse<Response> createResponse2 = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from("스탠다드 치킨"));

        ExtractableResponse<Response> listResponse = 메뉴_그룹_목록_조회_요청();

        메뉴_그룹_목록_조회됨(listResponse);
        메뉴_그룹_목록에_등록된_메뉴_그룹_포함됨(listResponse, Arrays.asList(createResponse1, createResponse2));
    }

    private void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 메뉴_그룹_목록에_등록된_메뉴_그룹_포함됨(ExtractableResponse<Response> listResponse,
                                         List<ExtractableResponse<Response>> createResponses) {

        List<MenuGroupRequest> MenuGroupRequests = listResponse.jsonPath().getList(".", MenuGroupRequest.class);
        List<MenuGroupRequest> createdMenuGroupRequests = createResponses.stream()
                .map(it -> it.as(MenuGroupRequest.class))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(MenuGroupRequests).hasSize(2),
                () -> assertThat(MenuGroupRequests).containsAll(createdMenuGroupRequests)
        );
    }
}
