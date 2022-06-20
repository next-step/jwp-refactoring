package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 그룹 관련 기능 인수테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    /**
     * Feature 메뉴 그룹 관련 기능
     *
     * Scenario 메뉴 그룹 관련 기능
     * Given 잘못된 이름
     * When 메뉴 그룹 등록 요청
     * Then 메뉴 그룹 등록 실패됨
     *
     * When 메뉴 그룹 등록 요청
     * Then 메뉴 그룹 등록됨
     * When 메뉴 그룹 목록 조회 요청
     * Then 메뉴 그룹 목록 조회됨
     */
    @Test
    @DisplayName("메뉴 그룹 관련 기능")
    void integrationTest() {
        //given
        String 잘못된_이름 = null;
        //when
        ExtractableResponse<Response> 잘못된_이름_메뉴_그룹_등록_응답_결과 = 메뉴_그룹_등록_요청(잘못된_이름);
        //then
        메뉴_그룹_등록_실패됨(잘못된_이름_메뉴_그룹_등록_응답_결과);

        //when
        ExtractableResponse<Response> 메뉴_그룹_등록_응답_결과 = 메뉴_그룹_등록_요청("추천메뉴");
        //then
        메뉴_그룹_등록됨(메뉴_그룹_등록_응답_결과);

        //when
        ExtractableResponse<Response> 메뉴_그룹_조회_응답_결과 = 메뉴_그룹_조회_요청();
        //then
        메뉴_그룹_목록_조회됨(메뉴_그룹_조회_응답_결과, "추천메뉴");
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response, String... name) {
        List<String> names = 메뉴_그룹_이름_목록_조회(response);
        assertThat(names).containsExactly(name);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
    
    private List<String> 메뉴_그룹_이름_목록_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", MenuGroup.class).stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return sendGet("/api/menu-groups");
    }

    private void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_그룹_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return sendPost("/api/menu-groups", menuGroup);
    }
}
