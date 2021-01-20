package kitchenpos.menugroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupAcceptanceTest extends MenuGroupAcceptanceTestSupport {

    @DisplayName("메뉴 그룹 생성")
    @Test
    void createMenuGroup() {
        // When
        MenuGroupRequest params = new MenuGroupRequest("추천메뉴");
        ExtractableResponse<Response> createResponse = 메뉴_그룹_등록_요청(params);

        // Then
        메뉴_그룹_생성_완료(createResponse);
    }

    @DisplayName("모든 메뉴 그룹 목록 조회")
    @Test
    void findMenuGroups() {
        // When
        ExtractableResponse<Response> findResponse = 메뉴_그룹_목록_조회_요청();

        // Then
        메뉴_그룹_응답(findResponse);
    }
}
