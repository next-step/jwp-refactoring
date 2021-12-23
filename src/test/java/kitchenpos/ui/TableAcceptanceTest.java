package kitchenpos.ui;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블을 관리한다.")
    @Test
    void manageTable() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("numberOfGuests", "0");
        params.put("empty", "true");

        // when
        ExtractableResponse<Response> createResponse = TableAcceptanceTestHelper.테이블_생성(params);

        // then
        TableAcceptanceTestHelper.테이블_생성됨(createResponse);

        // given
        int expected = 9;

        // when
        ExtractableResponse<Response> getResponse = TableAcceptanceTestHelper.테이블_조회();

        // then
        TableAcceptanceTestHelper.테이블_조회됨(getResponse);
        TableAcceptanceTestHelper.테이블_갯수_예상과_일치(getResponse, expected);

        // given
        Map<String, String> emptyParams = new HashMap<>();
        emptyParams.put("empty", "false");

        // when
        ExtractableResponse<Response> tableEmptyResponse =
            TableAcceptanceTestHelper.테이블_빈_테이블_여부_변경(1L, emptyParams);

        // then
        TableAcceptanceTestHelper.테이블_빈_테이블_여부_변경됨(tableEmptyResponse, false);

        // given
        Map<String, String> numberOfGuestParam = new HashMap<>();
        numberOfGuestParam.put("numberOfGuests", "4");

        // when
        ExtractableResponse<Response> numberGuestResponse =
            TableAcceptanceTestHelper.테이블_손님_명수_변경(1L, numberOfGuestParam);

        // then
        TableAcceptanceTestHelper.테이블_손님_명수_변경됨(numberGuestResponse, 4);
    }

    @DisplayName("인원수 0 미만시 변경 불가능")
    @Test
    void modifyNumberOfGuestFailWhenLessThanZero() {
        // given
        TableAcceptanceTestHelper.테이블_빈_테이블_여부_변경되어_있음(1L, "false");

        Map<String, String> params = new HashMap<>();
        params.put("numberOfGuests", "-1");

        // when
        ExtractableResponse<Response> response = TableAcceptanceTestHelper.테이블_손님_명수_변경(1L, params);

        // then
        TableAcceptanceTestHelper.테이블_손님_명수_변경_실패(response);
    }

    @DisplayName("주문 테이블이 비어있을 시 변경 불가능")
    @Test
    void modifyNumberOfGuestFailWhenTableNotExists() {
        // given
        TableAcceptanceTestHelper.테이블_빈_테이블_여부_변경되어_있음(99L, "false");

        Map<String, String> params = new HashMap<>();
        params.put("numberOfGuests", "4");

        // when
        ExtractableResponse<Response> response = TableAcceptanceTestHelper.테이블_손님_명수_변경(1L, params);

        // then
        TableAcceptanceTestHelper.테이블_손님_명수_변경_실패(response);
    }

    @DisplayName("테이블 그룹이 있을 시 변경 불가능")
    @Test
    void modifyEmptyFailWhenTableGroupExists() {
        // TODO
    }

    @DisplayName("요리 중이나 식사 중일 때 변경 불가능")
    @Test
    void modifyEmptyFailWhenCookingOrMeal() {
        // TODO
    }
}