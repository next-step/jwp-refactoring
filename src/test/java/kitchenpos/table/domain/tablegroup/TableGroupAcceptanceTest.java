package kitchenpos.table.domain.tablegroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.utils.AcceptanceTest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderTableDomainFixture.*;
import static kitchenpos.fixture.TableGroupDomainFixture.테이블_그룹_생성_요청;
import static kitchenpos.fixture.TableGroupDomainFixture.테이블_그룹_해제_요청;
import static kitchenpos.utils.AcceptanceFixture.응답_CREATE;
import static kitchenpos.utils.AcceptanceFixture.응답_NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인수 테스트 - 그룹 테이블 관리")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private void 테이블_그룹_등록됨(ExtractableResponse<Response> actual) {
        TableGroupResponse response = actual.as(TableGroupResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("테이블 그룹")
    public void group() {
        // given
        OrderTableResponse 한식_테이블_등록됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
        OrderTableResponse 양식_테이블_등록됨 = 주문_테이블_생성_요청(양식_테이블_요청).as(OrderTableResponse.class);
        TableGroupRequest 그룹_요청 = TableGroupRequest.from(Lists.newArrayList(한식_테이블_등록됨.getId(), 양식_테이블_등록됨.getId()));

        // when
        final ExtractableResponse<Response> actual = 테이블_그룹_생성_요청(그룹_요청);

        // then
        응답_CREATE(actual);
        테이블_그룹_등록됨(actual);
    }

    @Test
    @DisplayName("그룹 해제")
    public void ungroup() {
        // given
        OrderTableResponse 한식_테이블_등록됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
        OrderTableResponse 양식_테이블_등록됨 = 주문_테이블_생성_요청(양식_테이블_요청).as(OrderTableResponse.class);
        TableGroupRequest 그룹_요청 = TableGroupRequest.from(Lists.newArrayList(한식_테이블_등록됨.getId(), 양식_테이블_등록됨.getId()));
        final TableGroupResponse 그룹_등록됨 = 테이블_그룹_생성_요청(그룹_요청).as(TableGroupResponse.class);

        // when
        final ExtractableResponse<Response> actual = 테이블_그룹_해제_요청(그룹_등록됨.getId());

        // then
        응답_NO_CONTENT(actual);
    }
}
