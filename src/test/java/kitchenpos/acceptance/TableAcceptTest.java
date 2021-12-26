package kitchenpos.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.step.TableAcceptStep;
import kitchenpos.domain.OrderTable;

@DisplayName("테이블 인수테스트")
public class TableAcceptTest extends AcceptanceTest {

	@DisplayName("테이블 관리")
	@Test
	void 테이블_관리() {
		// given
		OrderTable 테이블_생성_요청_데이터 = new OrderTable();
		테이블_생성_요청_데이터.setNumberOfGuests(0);
		테이블_생성_요청_데이터.setEmpty(true);

		// when
		ExtractableResponse<Response> 테이블_생성_응답 = TableAcceptStep.테이블_생성_요청(테이블_생성_요청_데이터);

		// then
		OrderTable 생성된_테이블 = TableAcceptStep.테이블_생성_확인(테이블_생성_응답, 테이블_생성_요청_데이터);

		// when
		ExtractableResponse<Response> 테이블_목록_조회_응답 = TableAcceptStep.테이블_목록_조회_요청();

		// then
		TableAcceptStep.테이블_목록_조회_확인(테이블_목록_조회_응답, 생성된_테이블);

		// given
		OrderTable 테이블_상태_변경_요청_데이터 = new OrderTable();
		테이블_상태_변경_요청_데이터.setEmpty(false);

		// when
		ExtractableResponse<Response> 테이블_상태_변경_응답 = TableAcceptStep.테이블_상태_변경_요청(테이블_생성_응답, 테이블_상태_변경_요청_데이터);

		// then
		OrderTable 상태_변경된_테이블 = TableAcceptStep.테이블_상태_변경_확인(테이블_상태_변경_응답, 테이블_상태_변경_요청_데이터);

		// given
		OrderTable 테이블_인원_수정_요청_데이터 = new OrderTable();
		테이블_인원_수정_요청_데이터.setNumberOfGuests(4);

		// when
		ExtractableResponse<Response> 테이블_수정_응답 = TableAcceptStep.테이블_인원_수정_요청(테이블_생성_응답, 테이블_인원_수정_요청_데이터);

		// then
		OrderTable 인원_수정된_테이블 = TableAcceptStep.테이블_인원_수정_확인(테이블_수정_응답, 테이블_인원_수정_요청_데이터);

	}
}
