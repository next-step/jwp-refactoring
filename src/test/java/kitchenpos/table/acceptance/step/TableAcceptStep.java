package kitchenpos.table.acceptance.step;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.dto.TableEmptyUpdateRequest;
import kitchenpos.table.dto.TableGuestsUpdateRequest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.testutils.RestAssuredUtils;

public class TableAcceptStep {

	private static final String BASE_URL = "/api/tables";

	public static ExtractableResponse<Response> 테이블_생성_요청(TableRequest 요청_데이터_테이블) {
		return RestAssuredUtils.post(BASE_URL, 요청_데이터_테이블);
	}

	public static TableResponse 테이블_생성_확인(ExtractableResponse<Response> 테이블_생성_응답,
		TableRequest 테이블_생성_요청_데이터) {
		TableResponse 생성된_테이블 = 테이블_생성_응답.as(TableResponse.class);
		assertAll(
			() -> assertThat(테이블_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(생성된_테이블).satisfies(생성된_테이블_확인(테이블_생성_요청_데이터))
		);
		return 생성된_테이블;
	}

	private static Consumer<TableResponse> 생성된_테이블_확인(TableRequest 테이블_생성_요청_데이터) {
		return orderTable -> {
			assertThat(orderTable.getId()).isNotNull();
			assertThat(orderTable.getNumberOfGuests()).isEqualTo(테이블_생성_요청_데이터.getNumberOfGuests());
			assertThat(orderTable.isEmpty()).isEqualTo(테이블_생성_요청_데이터.isEmpty());
		};
	}

	public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
		return RestAssuredUtils.get(BASE_URL);
	}

	public static void 테이블_목록_조회_확인(ExtractableResponse<Response> 테이블_목록_조회_응답, TableResponse 생성된_테이블) {
		List<TableResponse> 조회된_테이블_목록 = 테이블_목록_조회_응답.as(new TypeRef<List<TableResponse>>() {
		});

		assertAll(
			() -> assertThat(테이블_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(조회된_테이블_목록).satisfies(조회된_테이블_목록_확인(생성된_테이블))
		);

	}

	private static Consumer<List<? extends TableResponse>> 조회된_테이블_목록_확인(TableResponse 생성된_테이블) {
		return orderTables -> {
			assertThat(orderTables.size()).isOne();
			assertThat(orderTables).first().satisfies(orderTable -> {
				assertThat(orderTable.getId()).isEqualTo(생성된_테이블.getId());
				assertThat(orderTable.getNumberOfGuests()).isEqualTo(생성된_테이블.getNumberOfGuests());
				assertThat(orderTable.isEmpty()).isEqualTo(생성된_테이블.isEmpty());
			});
		};
	}

	public static ExtractableResponse<Response> 테이블_인원_수정_요청(ExtractableResponse<Response> 생성된_테이블,
		TableGuestsUpdateRequest 테이블_인원_수정_요청_데이터) {
		String url = 생성된_테이블.header("Location") + "/number-of-guests";
		return RestAssuredUtils.put(url, 테이블_인원_수정_요청_데이터);
	}

	public static TableResponse 테이블_인원_수정_확인(ExtractableResponse<Response> 테이블_수정_응답,
		TableGuestsUpdateRequest 테이블_수정_요청_데이터) {
		TableResponse 수정된_테이블 = 테이블_수정_응답.as(TableResponse.class);
		assertAll(
			() -> assertThat(테이블_수정_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(수정된_테이블.getNumberOfGuests()).isEqualTo(테이블_수정_요청_데이터.getNumberOfGuests())
		);
		return 수정된_테이블;
	}

	public static ExtractableResponse<Response> 테이블_상태_변경_요청(ExtractableResponse<Response> 테이블_생성_응답,
		TableEmptyUpdateRequest 테이블_상태_변경_요청_데이터) {
		String url = 테이블_생성_응답.header("Location") + "/empty";
		return RestAssuredUtils.put(url, 테이블_상태_변경_요청_데이터);
	}

	public static TableResponse 테이블_상태_변경_확인(ExtractableResponse<Response> 테이블_상태_변경_응답,
		TableEmptyUpdateRequest 테이블_상태_변경_요청_데이터) {
		TableResponse 상태_변경된_테이블 = 테이블_상태_변경_응답.as(TableResponse.class);
		assertAll(
			() -> assertThat(테이블_상태_변경_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(상태_변경된_테이블.isEmpty()).isEqualTo(테이블_상태_변경_요청_데이터.isEmpty())
		);
		return 상태_변경된_테이블;
	}

	public static TableResponse 테이블_등록_되어_있음(int numberOfGuests, boolean isEmpty) {
		TableRequest orderTable = new TableRequest(numberOfGuests, isEmpty);
		return 테이블_생성_요청(orderTable).as(TableResponse.class);
	}
}
