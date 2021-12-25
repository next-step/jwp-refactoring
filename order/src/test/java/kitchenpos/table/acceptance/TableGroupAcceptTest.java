package kitchenpos.table.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.acceptance.step.TableAcceptStep;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kitchenpos.table.acceptance.step.TableGroupAcceptStep.단체_지정_요청;
import static kitchenpos.table.acceptance.step.TableGroupAcceptStep.단체_지정_확인;
import static kitchenpos.table.acceptance.step.TableGroupAcceptStep.단체_해지_요청;
import static kitchenpos.table.acceptance.step.TableGroupAcceptStep.단체_해지_확인;

@DisplayName("단체 인수테스트")
class TableGroupAcceptTest extends AcceptanceTest {

    @DisplayName("단체를 관리한다")
    @Test
    void 단체를_관리한다() {
        // given
        TableResponse 일번_테이블 = TableAcceptStep.테이블이_등록되어_있음(2, true);
        TableResponse 이번_테이블 = TableAcceptStep.테이블이_등록되어_있음(4, true);

        List<Long> 요청할_테이블_ID_목록 = Stream.of(일번_테이블, 이번_테이블)
                .map(TableResponse::getId)
                .collect(Collectors.toList());

        TableGroupRequest 단체_등록_요청_데이터 = TableGroupRequest.of(요청할_테이블_ID_목록);

        // when
        ExtractableResponse<Response> 단체_등록_응답 = 단체_지정_요청(단체_등록_요청_데이터);

        // then
        단체_지정_확인(단체_등록_응답, 단체_등록_요청_데이터);

        // when
        ExtractableResponse<Response> 단체_해지_응답 = 단체_해지_요청(단체_등록_응답);

        /// then
        단체_해지_확인(단체_해지_응답);
    }
}
