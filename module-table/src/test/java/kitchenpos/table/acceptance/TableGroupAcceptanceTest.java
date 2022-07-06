package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_주문_번호_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.common.utils.RestAssuredHelper;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("단체 그룹 인수테스트 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private static final String TABLE_GROUP_URI = "/api/table-groups";
    private ExtractableResponse<Response> 첫번째_테이블_주문_번호_결과, 두번째_테이블_주문_번호_결과;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        첫번째_테이블_주문_번호_결과 = 테이블_주문_번호_생성_요청(3, true);
        두번째_테이블_주문_번호_결과 = 테이블_주문_번호_생성_요청(5, true);
    }

    /**
     *  Given 주문 테이블이 주어지고
     *  When  주문 테이블을 그룹핑하면
     *  Then  주문 테이블이 그룹핑된다.
     */
    @Test
    @DisplayName("주문 테이블을 묶으면 단체 그룹으로 묶인다.")
    void createTableGroup() {
        // when
        final ExtractableResponse<Response> 테이블_단체_그룹_요청_결과 = 테이블_단체_그룹_요청(
                Arrays.asList(첫번째_테이블_주문_번호_결과, 두번째_테이블_주문_번호_결과));

        // then
        테이블_단체_그룹_요청_확인(테이블_단체_그룹_요청_결과);
    }

    /**
     *  Given 주문 테이블이 주어지고
     *    And 그룹화되어 있고
     *   When 주문 테이블이 그룹화를 해제하면
     *   Then 주문 테이블이 그룹화가 해제된다.
     */
    @Test
    @DisplayName("단체 그룹을 그룹화 해제한다.")
    void ungroupTableGroup() {
        // given
        테이블_단체_그룹_요청(Arrays.asList(첫번째_테이블_주문_번호_결과, 두번째_테이블_주문_번호_결과));

        // when
        final ExtractableResponse<Response> 테이블_단체_그룹_해제_요청_결과 = 테이블_단체_그룹_해제_요청(1L);

        // then
        테이블_단체_그룹_해제_요청_확인(테이블_단체_그룹_해제_요청_결과);
    }

    public static ExtractableResponse<Response> 테이블_단체_그룹_요청(List<ExtractableResponse<Response>> 테이블_주문_결과들) {
        final List<Long> 테이블_주문 = 테이블_주문_결과들.stream()
                .map(it -> it.body().jsonPath().getObject(".", OrderTableResponse.class))
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());
        return RestAssuredHelper.post(TABLE_GROUP_URI, new TableGroupRequest(테이블_주문));
    }

    public static void 테이블_단체_그룹_요청_확인(ExtractableResponse<Response> 테이블_단체_그룹_요청_결과) {
        assertThat(테이블_단체_그룹_요청_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 테이블_단체_그룹_해제_요청(Long 해제할_그룹_아이디) {
        String uri = TABLE_GROUP_URI + "/{tableGroupId}";
        return RestAssuredHelper.delete(uri, 해제할_그룹_아이디);
    }

    public static void 테이블_단체_그룹_해제_요청_확인(ExtractableResponse<Response> 테이블_단체_그룹_해제_요청_결과) {
        assertThat(테이블_단체_그룹_해제_요청_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
