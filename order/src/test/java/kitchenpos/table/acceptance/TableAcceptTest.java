package kitchenpos.table.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.ChangeTableEmptyRequest;
import kitchenpos.table.dto.ChangeTableGuestRequest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.table.acceptance.step.TableAcceptStep.테이블_등록_요청;
import static kitchenpos.table.acceptance.step.TableAcceptStep.테이블_등록_확인;
import static kitchenpos.table.acceptance.step.TableAcceptStep.테이블_목록_조회_요청;
import static kitchenpos.table.acceptance.step.TableAcceptStep.테이블_목록_조회_확인;
import static kitchenpos.table.acceptance.step.TableAcceptStep.테이블_상태_변경_요청;
import static kitchenpos.table.acceptance.step.TableAcceptStep.테이블_상태_확인;
import static kitchenpos.table.acceptance.step.TableAcceptStep.테이블_인원_변경_요청;
import static kitchenpos.table.acceptance.step.TableAcceptStep.테이블_인원_확인;

@DisplayName("테이블 인수테스트")
class TableAcceptTest extends AcceptanceTest {
    @DisplayName("테이블을 관리한다")
    @Test
    void 테이블을_관리한다() {
        // given
        TableRequest 테이블_등록_요청_데이터 = TableRequest.of(0, true);

        // when
        ExtractableResponse<Response> 테이블_등록_응답 = 테이블_등록_요청(테이블_등록_요청_데이터);

        // then
        TableResponse 등록된_테이블 = 테이블_등록_확인(테이블_등록_응답, 테이블_등록_요청_데이터);

        // when
        ExtractableResponse<Response> 테이블_목록_조회_응답 = 테이블_목록_조회_요청();

        // then
        테이블_목록_조회_확인(테이블_목록_조회_응답, 등록된_테이블);

        // given
        ChangeTableEmptyRequest 테이블_비어있지_않음_요청_데이터 = ChangeTableEmptyRequest.of(false);

        // when
        ExtractableResponse<Response> 테이블_비어있지_않음_응답 = 테이블_상태_변경_요청(테이블_등록_응답, 테이블_비어있지_않음_요청_데이터);

        // then
        테이블_상태_확인(테이블_비어있지_않음_응답, 테이블_비어있지_않음_요청_데이터);

        // given
        ChangeTableGuestRequest 테이블_인원_변경_요청_데이터 = ChangeTableGuestRequest.of(4);

        // when
        ExtractableResponse<Response> 테이블_인원_변경_응답 = 테이블_인원_변경_요청(테이블_등록_응답, 테이블_인원_변경_요청_데이터);

        // then
        테이블_인원_확인(테이블_인원_변경_응답, 테이블_인원_변경_요청_데이터);

        // given
        ChangeTableEmptyRequest 테이블_비어있음_요청_데이터 = ChangeTableEmptyRequest.of(true);

        // when
        ExtractableResponse<Response> 테이블_비어있음_않음_응답 = 테이블_상태_변경_요청(테이블_등록_응답, 테이블_비어있음_요청_데이터);

        // then
        테이블_상태_확인(테이블_비어있음_않음_응답, 테이블_비어있음_요청_데이터);
    }
}
