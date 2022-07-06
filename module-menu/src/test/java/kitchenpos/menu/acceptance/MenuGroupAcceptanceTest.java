package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 그룹 인수테스트 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    private static final String MENU_GROUPS_URI = "/api/menu-groups";

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    /**
     *  When 메뉴 그룹을 만들면
     *  Then 메뉴 그룹들을 조회할 수 있다
     */
    @Test
    @DisplayName("메뉴 그룹을 만들면 조회할 수 있다.")
    void searchMenuGroups() {
        // when
        final ExtractableResponse<Response> 메뉴_그룹_생성_결과 = 메뉴_그룹_생성_요청("추천메뉴");
        메뉴_그룹_생성_확인(메뉴_그룹_생성_결과);

        // then
        final ExtractableResponse<Response> 메뉴_그룹_조회_결과 = 메뉴_그룹_조회();
        메뉴_그룹_조회_확인(메뉴_그룹_조회_결과, Collections.singletonList("추천메뉴"));
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String 메뉴_그룹명) {
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest(메뉴_그룹명);
        return RestAssuredHelper.post(MENU_GROUPS_URI, menuGroupRequest);
    }

    public static void 메뉴_그룹_생성_확인(ExtractableResponse<Response> 메뉴_그룹_생성_결과) {
        assertThat(메뉴_그룹_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회() {
        return RestAssuredHelper.get(MENU_GROUPS_URI);
    }

    private void 메뉴_그룹_조회_확인(ExtractableResponse<Response> 메뉴_그룹_조회_결과, List<String> 예상된_메뉴_그룹_이름) {
        final List<String> 실제_메뉴_그룹_이름 = 메뉴_그룹_조회_결과.body().jsonPath().getList("name", String.class);

        assertAll(
                () -> assertThat(메뉴_그룹_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(실제_메뉴_그룹_이름).isEqualTo(예상된_메뉴_그룹_이름)
        );
    }
}
