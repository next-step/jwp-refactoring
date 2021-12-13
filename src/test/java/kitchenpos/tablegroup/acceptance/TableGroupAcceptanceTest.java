package kitchenpos.tablegroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.ordertable.acceptance.OrderTableAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("테이블 그룹 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 테이블1;
    private OrderTableResponse 테이블2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        테이블1 = 테이블_등록되어_있음(new OrderTableRequest(2, true));
        테이블2 = 테이블_등록되어_있음(new OrderTableRequest(2, true));
    }

    @Test
    @DisplayName("테이블 그룹을 관리한다.")
    void manageTableGroup() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                LocalDateTime.now(), Arrays.asList(테이블1.getId(), 테이블2.getId()));

        // when
        ExtractableResponse<Response> createResponse = 테이블_그룹_등록_요청(tableGroupRequest);

        // then
        테이블_그룹_등록됨(createResponse);

        // when
        ExtractableResponse<Response> ungroupResponse = 테이블_그룹에서_테이블_제거_요청(extractId(createResponse));

        // then
        테이블_그룹에서_테이블_제거됨(ungroupResponse);
    }

    public static TableGroupResponse 테이블_그룹_등록되어_있음(TableGroupRequest tableGroupRequest) {
        return post("/api/table-groups", tableGroupRequest).as(TableGroupResponse.class);
    }

    public static ExtractableResponse<Response> 테이블_그룹_등록_요청(TableGroupRequest tableGroupRequest) {
        return post("/api/table-groups", tableGroupRequest);
    }

    public static ExtractableResponse<Response> 테이블_그룹에서_테이블_제거_요청(Long id) {
        return delete("/api/table-groups/{tableGroupId}", id);
    }

    private Long extractId(ExtractableResponse<Response> createResponse) {
        return createResponse.as(TableGroupResponse.class).getId();
    }

    private void 테이블_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 테이블_그룹에서_테이블_제거됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
