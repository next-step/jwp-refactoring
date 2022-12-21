package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.rest.TableGroupRestAssured;
import kitchenpos.rest.TableRestAssured;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.dto.TableRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;

public class TableGroupAcceptanceTest extends BaseAcceptanceTest {

    private TableGroupCreateRequest request;

    @BeforeEach
    public void setUp() {
        super.setUp();

        List<OrderTableResponse> orderTableResponses = Arrays.asList(
                TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(1, true)),
                TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(2, true)),
                TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(3, true))
        );

        List<TableRequest> tableRequests = orderTableResponses.stream()
                .map(orderTableResponse -> new TableRequest(orderTableResponse.getId()))
                .collect(Collectors.toList());
        this.request = new TableGroupCreateRequest(tableRequests);
    }

    @Test
    @DisplayName("테이블 그룹 등록 요청시 요청에 성공한다")
    void createTableGroupThenReturnResponses() {
        // when
        ExtractableResponse<Response> response = TableGroupRestAssured.주문_테이블_그룹_지정_요청(request);

        // then
        TableGroupResponse tableGroupResponse = response.as(TableGroupResponse.class);
        assertAll(
                () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> Assertions.assertThat(tableGroupResponse.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("테이블 그룹 해지 요청시 요청에 성공한다")
    void unGroupTablesTest() {
        // given
        TableGroupResponse tableGroup = TableGroupRestAssured.주문_테이블_그룹_지정_요청(request).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = TableGroupRestAssured.주문_테이블_그룹_해지_요청(tableGroup.getId());

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
