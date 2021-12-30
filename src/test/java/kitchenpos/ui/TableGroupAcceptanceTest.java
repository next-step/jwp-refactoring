package kitchenpos.ui;

import static kitchenpos.ui.TableAcceptanceTest.주문테이블_생성;
import static kitchenpos.utils.AcceptanceTestUtil.delete;
import static kitchenpos.utils.AcceptanceTestUtil.post;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.dto.TableGroupsRequest;
import kitchenpos.tablegroup.dto.TableGroupsRequest.OrderTableRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 테이블1;
    private OrderTableResponse 테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        테이블1 = 주문테이블_생성(2, true);
        테이블2 = 주문테이블_생성(3, true);
    }

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void createTableGroup() {
        // given
        List<OrderTableRequest> 그룹화할_테이블 = Lists.newArrayList(
            new OrderTableRequest(테이블1.getId()), new OrderTableRequest(테이블2.getId()));

        TableGroupsRequest 테이블_그룹화_요청 = new TableGroupsRequest(그룹화할_테이블);

        // when
        ExtractableResponse<Response> 테이블_그룹_생성_응답 = 테이블_그룹_생성(테이블_그룹화_요청);

        // then
        테이블_그룹_생성됨(테이블_그룹_생성_응답);
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void unGroup() {
        // given
        List<OrderTableRequest> 그룹화할_테이블 = Lists.newArrayList(
            new OrderTableRequest(테이블1.getId()), new OrderTableRequest(테이블2.getId()));
        TableGroupsRequest 테이블_그룹화_요청 = new TableGroupsRequest(그룹화할_테이블);
        TableGroupResponse 생성된_테이블_그룹 = 테이블_그룹_생성(테이블_그룹화_요청).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> 테이블그룹_해제_응답 = 테이블그룹_해제(생성된_테이블_그룹);

        // then
        테이블그룹_해제됨(테이블그룹_해제_응답);

    }

    private ExtractableResponse<Response> 테이블_그룹_생성(TableGroupsRequest tableGroups) {
        return post("/api/table-groups", tableGroups);
    }

    private void 테이블_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        TableGroupResponse tableGroup = response.as(TableGroupResponse.class);
        assertThat(tableGroup.getOrderTables()).extracting(OrderTableResponse::getId)
            .contains(테이블1.getId(), 테이블2.getId());
    }

    private ExtractableResponse<Response> 테이블그룹_해제(TableGroupResponse tableGroup) {
        return delete("/api/table-groups/" + tableGroup.getId());
    }

    private void 테이블그룹_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}