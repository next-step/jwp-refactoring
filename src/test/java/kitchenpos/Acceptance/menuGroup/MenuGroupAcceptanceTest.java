package kitchenpos.Acceptance.menuGroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.Acceptance.utils.RestAssuredRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/api/menu-groups";

    @DisplayName("메뉴 그룹 생성 요청 시 정상 생성되어야 한다")
    @Test
    void menuGroupCreateTest() {
        // given
        String 메뉴_그룹_이름 = "메뉴 그룹";

        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_결과 = 메뉴_그룹_생성_요청(메뉴_그룹_이름);

        // then
        메뉴_그룹_정상_생성됨(메뉴_그룹_생성_결과, 메뉴_그룹_이름);
    }

    @DisplayName("메뉴 그룹 목록 조회 시 정상 조회되어야 한다")
    @Test
    void menuGroupFindAllTest() {
        // given
        List<String> 메뉴_그룹_이름들 = Arrays.asList("메뉴 그룹 1", "메뉴 그룹 2", "메뉴 그룹 3", "메뉴 그룹 4", "메뉴 그룹 5");
        메뉴_그룹_이름들.forEach(MenuGroupAcceptanceTest::메뉴_그룹_생성_요청);

        // when
        ExtractableResponse<Response> 메뉴_그룹_목록_조회_결과 = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_목록_정상_조회됨(메뉴_그룹_목록_조회_결과, 메뉴_그룹_이름들);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
        Map<String, Object> body = new HashMap<>();

        body.put("name", name);

        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), body);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssuredRequest.getRequest(PATH, Collections.emptyMap());
    }

    void 메뉴_그룹_정상_생성됨(ExtractableResponse<Response> response, String expectedName) {
        MenuGroup menuGroup = response.as(MenuGroup.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(menuGroup.getName()).isEqualTo(expectedName);
        assertThat(menuGroup.getId()).isNotNull();
    }

    void 메뉴_그룹_목록_정상_조회됨(ExtractableResponse<Response> response, List<String> expectedNames) {
        List<String> names = response.body().jsonPath().getList("name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(names).containsAll(expectedNames);
    }
}
