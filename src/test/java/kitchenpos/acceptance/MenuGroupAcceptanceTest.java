package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.utils.Http;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 관리 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    @DisplayName("메뉴 그룹을 관리한다")
    @Test
    void testCreate() {
        // given
        MenuGroup 식사류 = new MenuGroup("식사류");

        // when
        ExtractableResponse<Response> createResponse = 메뉴_그룹_생성_요청(식사류);
        // then
        메뉴_그룹_생성됨(createResponse);

        // when
        ExtractableResponse<Response> listResponse = 모든_메뉴_그룹_조회_요청();
        // then
        모든_메뉴_그룹_조회_응답됨(listResponse);
        생성된_메뉴_그룹이_포함됨(식사류, listResponse);
    }

    /**
     * 요청 관련
     */
    private static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroup menuGroup) {
        return Http.post("/api/menu-groups", menuGroup);
    }

    private static ExtractableResponse<Response> 모든_메뉴_그룹_조회_요청() {
        return Http.get("/api/menu-groups");
    }

    /**
     * 응답 관련
     */
    private static void 메뉴_그룹_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 모든_메뉴_그룹_조회_응답됨(ExtractableResponse<Response> listResponse) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 생성된_메뉴_그룹이_포함됨(MenuGroup 식사류, ExtractableResponse<Response> listResponse) {
        List<MenuGroup> menuGroups = listResponse.jsonPath().getList(".", MenuGroup.class);
        assertThat(menuGroups).map(MenuGroup::getName)
                .containsExactly(식사류.getName());
    }

    /**
     * 테스트 픽스처 관련
     */
    public static MenuGroup 메뉴_그룹_등록되어_있음(String name) {
        return 메뉴_그룹_생성_요청(new MenuGroup(name)).as(MenuGroup.class);
    }
}
