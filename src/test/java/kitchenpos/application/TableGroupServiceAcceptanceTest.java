package kitchenpos.application;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


class TableGroupServiceAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블 그룹을 등록한다")
    @Test
    void createTest() {

        ExtractableResponse<Response> getResponse = TableFactory.주문테이블_조회_요청();
        List<OrderTableResponse> orderTables = Arrays.asList(getResponse.as(OrderTableResponse[].class));

        List<Long> orderTableIds = orderTables.stream().map(OrderTableResponse::getId).collect(Collectors.toList());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        ExtractableResponse<Response> createTableGroupResponse = TableGroupFactory.주문그룹테이블_생성_요청(tableGroupRequest);
        TableGroupFactory.주문그룹테이블이_생성됨(createTableGroupResponse);

    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void unGroupTest() {

        ExtractableResponse<Response> getResponse = TableFactory.주문테이블_조회_요청();
        List<OrderTableResponse> orderTables = Arrays.asList(getResponse.as(OrderTableResponse[].class));

        List<Long> orderTableIds = orderTables.stream().map(OrderTableResponse::getId).collect(Collectors.toList());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        ExtractableResponse<Response> createTableGroupResponse = TableGroupFactory.주문그룹테이블_생성_요청(tableGroupRequest);
        TableGroupResponse createdTableGroup = TableGroupFactory.주문그룹테이블이_생성됨(createTableGroupResponse);

        ExtractableResponse<Response> deleteTableGroupResponse = TableGroupFactory.주문그룹테이블_삭제_요청(createdTableGroup.getId());
        assertThat(deleteTableGroupResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

}
