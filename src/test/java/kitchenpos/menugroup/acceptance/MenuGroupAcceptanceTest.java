package kitchenpos.menugroup.acceptance;

import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

@DisplayName("메뉴 그룹 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    /**
     * when 메뉴 그룹 등록 요청
     * then 메뉴 그룹 등록 완료
     * when 메뉴 그룹 목록 조회 요청
     * then 메뉴 그룹 목록 조회 완료
     */
    @DisplayName("메뉴 그룹 관리")
    @Test
    void menuGroup() {
        // given
        String name = "메뉴 그룹";
        // when
        ExtractableResponse<Response> 메뉴_그룹_등록_요청 = 메뉴_그룹_등록_요청(name);
        // then
        메뉴_그룹_등록_완료(메뉴_그룹_등록_요청);
        // when
        ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청 = 메뉴_그룹_목록_조회_요청();
        // then
        메뉴_그룹_목록_조회_완료(메뉴_그룹_목록_조회_요청);
        assertThat(메뉴_그룹_목록_이름_추출(메뉴_그룹_목록_조회_요청)).contains(name);
    }

    private List<String> 메뉴_그룹_목록_이름_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }
}
