package kitchenpos.tablegroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.RestAssuredFixture.*;
import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_등록되어_있음;

@DisplayName("테이블 그룹 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/table-groups";

    private OrderTableResponse 테이블1;
    private OrderTableResponse 테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        테이블1 = 주문_테이블_등록되어_있음(6, true);
        테이블2 = 주문_테이블_등록되어_있음(4, true);
    }

    @DisplayName("테이블 그룹을 관리한다.")
    @Test
    void manageTableGroup() {
        // given
        TableGroupRequest tableGroupRequest = TableGroupRequest.of(Arrays.asList(OrderTableIdRequest.of(테이블1.getId()), OrderTableIdRequest.of(테이블2.getId())));

        // when
        ExtractableResponse<Response> 테이블_그룹_생성_응답 = 테이블_그룹_생성_요청(tableGroupRequest);
        // then
        테이블_그룹_생성됨(테이블_그룹_생성_응답);

        // when
        Long 테이블_그룹_ID = 테이블_그룹_ID_조회(테이블_그룹_생성_응답);
        ExtractableResponse<Response> 테이블_그룹_해제_응답 = 테이블_그룹_해제_요청(테이블_그룹_ID);
        // then
        테이블_그룹_해제됨(테이블_그룹_해제_응답);
    }

    private ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroupRequest params) {
        return 생성_요청(API_URL, params);
    }

    private void 테이블_그룹_생성됨(ExtractableResponse<Response> response) {
        생성됨_201_CREATED(response);
    }

    private Long 테이블_그룹_ID_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 테이블_그룹_해제_요청(Long tableGroupId) {
        String path = String.format("%s/%s", API_URL, tableGroupId);

        return 삭제_요청(path);
    }

    private void 테이블_그룹_해제됨(ExtractableResponse<Response> response) {
        콘텐츠_없음_204_NO_CONTENT(response);
    }
}
