package kitchenpos.tablegroup.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.ui.OrderAcceptanceTestHelper;
import kitchenpos.table.ui.TableAcceptanceTestHelper;

class TableGroupAcceptanceTest extends AcceptanceTest {
    @DisplayName("주문 테이블 그룹을 관리한다.")
    @Test
    void manageTableGroup() {
        // given
        Map<String, Object> createParams = new HashMap<>();
        createParams.put("orderTables",
            Arrays.asList(Collections.singletonMap("id", 1), Collections.singletonMap("id", 2))
        );

        // when
        ExtractableResponse<Response> createResponse = TableGroupAcceptanceTestHelper.테이블_그룹을_생성(createParams);

        // then
        TableGroupAcceptanceTestHelper.테이블_그룹_생성됨(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = TableGroupAcceptanceTestHelper.테이블_그룹_삭제(1L);

        // then
        TableGroupAcceptanceTestHelper.테이블_그룹_삭제됨(deleteResponse);
    }

    @DisplayName("주문 테이블이 없거나 2개 미만일 시 생성 불가능")
    @Test
    void createTableGroupFailWhenOrderTablesSizeIsLessThanTwo() {
        // given
        Map<String, Object> createParams = new HashMap<>();
        createParams.put("orderTables",
            Arrays.asList(Collections.singletonMap("id", 1))
        );

        // when
        ExtractableResponse<Response> createResponse = TableGroupAcceptanceTestHelper.테이블_그룹을_생성(createParams);

        // then
        TableGroupAcceptanceTestHelper.테이블_그룹_생성_실패(createResponse);
    }

    @DisplayName("입력받은 주문 테이블 개수와 실제 주문 테이블 개수가 다른 경우(메뉴에 없는 주문 테이블) 생성 불가능")
    @Test
    void createTableGroupFailWhenTableNumberIsDifferent() {
        // given
        Map<String, Object> createParams = new HashMap<>();
        createParams.put("orderTables",
            Arrays.asList(Collections.singletonMap("id", 1), Collections.singletonMap("id", 99))
        );

        // when
        ExtractableResponse<Response> createResponse = TableGroupAcceptanceTestHelper.테이블_그룹을_생성(createParams);

        // then
        TableGroupAcceptanceTestHelper.테이블_그룹_생성_실패(createResponse);
    }

    @DisplayName("사용중인 테이블이 포함되어 있는 경우 생성 불가능")
    @Test
    void createTableGroupFailWhenOrderTableIsEmpty() {
        // given
        TableAcceptanceTestHelper.테이블_빈_테이블_여부_변경되어_있음(1L, "false");
        Map<String, Object> createParams = new HashMap<>();
        createParams.put("orderTables",
            Arrays.asList(Collections.singletonMap("id", 1), Collections.singletonMap("id", 2))
        );

        // when
        ExtractableResponse<Response> createResponse = TableGroupAcceptanceTestHelper.테이블_그룹을_생성(createParams);

        // then
        TableGroupAcceptanceTestHelper.테이블_그룹_생성_실패(createResponse);
    }

    @DisplayName("다른 그룹에 등록되어 있는 주문 테이블이 포함되어 있는 경우 생성 불가능")
    @Test
    void createTableGroupFailWhenOrderTableIsInOtherGroup() {
        // given
        List<Map<String, Integer>> tableIds = Arrays.asList(Collections.singletonMap("id", 1),
            Collections.singletonMap("id", 2));
        TableGroupAcceptanceTestHelper.테이블_그룹_생성되어_있음(tableIds);

        Map<String, Object> createParams = new HashMap<>();
        createParams.put("orderTables", tableIds);

        // when
        ExtractableResponse<Response> createResponse = TableGroupAcceptanceTestHelper.테이블_그룹을_생성(createParams);

        // then
        TableGroupAcceptanceTestHelper.테이블_그룹_생성_실패(createResponse);
    }

    @DisplayName("요리 중이나 식사 중인 주문 테이블을 포함하고 있다면 삭제 불가능")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COOKING"})
    void deleteTableGroupFailWhenContainsMealOrCooking(String orderStatus) {
        // given
        List<Map<String, Integer>> tableIds = Arrays.asList(Collections.singletonMap("id", 1),
            Collections.singletonMap("id", 2));
        TableGroupAcceptanceTestHelper.테이블_그룹_생성되어_있음(tableIds);

        Map<String, Integer> orderLineItem = new HashMap<>();
        orderLineItem.put("menuId", 1);
        orderLineItem.put("quantity", 1);
        OrderAcceptanceTestHelper.주문_생성되어_있음(Arrays.asList(orderLineItem), 1);
        OrderAcceptanceTestHelper.주문_상태_변경되어_있음(1, orderStatus);

        // when
        ExtractableResponse<Response> deleteResponse = TableGroupAcceptanceTestHelper.테이블_그룹_삭제(1L);

        // then
        TableGroupAcceptanceTestHelper.테이블_그룹_삭제_실패(deleteResponse);
    }
}