package kitchenpos.menugroup.ui;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 관리한다.")
    @Test
    void manageMenuGroup() {
        // given
        Map<String, String> params = new HashMap<>();
        String name = "추천메뉴";
        params.put("name", name);

        // when
        ExtractableResponse<Response> createResponse = MenuGroupAcceptanceTestHelper.메뉴_그룹_생성_요청(params);

        // then
        MenuGroupAcceptanceTestHelper.메뉴_그룹_생성됨(createResponse);
        MenuGroupAcceptanceTestHelper.메뉴_그룹_예상괴_일치(createResponse, name);

        // given
        int expectedLength = 5;

        // when
        ExtractableResponse<Response> getResponse = MenuGroupAcceptanceTestHelper.메뉴_그룹_조회_요청();

        // then
        MenuGroupAcceptanceTestHelper.메뉴_그룹_조회됨(getResponse);
        MenuGroupAcceptanceTestHelper.메뉴_그룹_갯수_예상과_일치(getResponse, expectedLength);
    }

}