package kitchenpos.table.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.common.AcceptanceFixture.CREATE_응답_잘_받음;
import static kitchenpos.common.AcceptanceFixture.응답_NO_CONTENT;
import static kitchenpos.fixture.OrderTableAcceptanceFixture.*;
import static kitchenpos.fixture.TableGroupAcceptanceFixture.테이블_그룹_생성_요청;
import static kitchenpos.fixture.TableGroupAcceptanceFixture.테이블_그룹_해제_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 관련(단체지정)(인수 테스트)")
class TableGroupAcceptanceTest extends AcceptanceTest {
    OrderTableResponse 테이블_6인_요청_등록됨;
    OrderTableResponse 테이블_4인_요청_등록됨;
    TableGroupRequest 그룹_요청;

    @BeforeEach
    public void setUp() {
        super.setUp();
        테이블_6인_요청_등록됨 = 주문_테이블_생성_요청(테이블_6인_요청).as(OrderTableResponse.class);
        테이블_4인_요청_등록됨 = 주문_테이블_생성_요청(테이블_4인_요청).as(OrderTableResponse.class);
        그룹_요청 = TableGroupRequest.from(Lists.newArrayList(테이블_6인_요청_등록됨.getId(), 테이블_4인_요청_등록됨.getId()));
    }

    @DisplayName("그룹 테이블 생성 하기")
    @Test
    public void groupTest() {
        ExtractableResponse<Response> actual = 테이블_그룹_생성_요청(그룹_요청);
        CREATE_응답_잘_받음(actual);
        테이블_그룹_등록_확인(actual);
    }

    @DisplayName("그룹 테이블 제거 하기")
    @Test
    public void ungroupTest() {
        TableGroupResponse 그룹_등록됨 = 테이블_그룹_생성_요청(그룹_요청).as(TableGroupResponse.class);
        응답_NO_CONTENT(테이블_그룹_해제_요청(그룹_등록됨.getId()));
    }

    private void 테이블_그룹_등록_확인(ExtractableResponse<Response> actual) {
        TableGroupResponse response = actual.as(TableGroupResponse.class);
        assertThat(response).isNotNull();
    }
}
