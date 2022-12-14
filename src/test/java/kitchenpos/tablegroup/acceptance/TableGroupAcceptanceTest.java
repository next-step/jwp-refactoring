package kitchenpos.tablegroup.acceptance;


import static kitchenpos.ordertable.acceptance.TableAcceptanceTestFixture.주문_테이블_생성되어_있음;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTestFixture.테이블_그룹_생성_요청;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTestFixture.테이블_그룹_생성되어_있음;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTestFixture.테이블_그룹_생성됨;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTestFixture.테이블_그룹_해제_요청;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTestFixture.테이블_그룹_해제됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.ordertable.dto.TableRequest;
import kitchenpos.ordertable.dto.TableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest.GroupTableRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 그룹 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private TableResponse 주문테이블_A;
    private TableResponse 주문테이블_B;
    private TableGroupRequest 단체테이블;

    @BeforeEach
    void tableGroupSetUp() {
        super.setUp();

        주문테이블_A = 주문_테이블_생성되어_있음(new TableRequest(5, true)).as(TableResponse.class);
        주문테이블_B = 주문_테이블_생성되어_있음(new TableRequest(6, true)).as(TableResponse.class);

        단체테이블 = new TableGroupRequest(Arrays.asList(new GroupTableRequest(주문테이블_A.getId()), new GroupTableRequest(주문테이블_B.getId())));
    }

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        ExtractableResponse<Response> response = 테이블_그룹_생성_요청(단체테이블);

        테이블_그룹_생성됨(response);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        TableGroupResponse tableGroup = 테이블_그룹_생성되어_있음(단체테이블).as(TableGroupResponse.class);

        ExtractableResponse<Response> response = 테이블_그룹_해제_요청(tableGroup.getId());

        테이블_그룹_해제됨(response);
    }
}
