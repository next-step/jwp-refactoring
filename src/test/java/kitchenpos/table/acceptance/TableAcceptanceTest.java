package kitchenpos.table.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.*;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.*;
import static kitchenpos.table.acceptance.TableGroupAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@DisplayName("주문 테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

	private OrderTableRequest 빈_테이블_입력 = OrderTableRequest.of(2, true);
	private OrderTableRequest 채워진_테이블_입력 = OrderTableRequest.of(2, false);

	@DisplayName("주문 테이블 기능 통합 테스트")
	@TestFactory
	Stream<DynamicNode> table() {
		return Stream.of(
			dynamicTest("빈 테이블을 등록한다.", () -> {
				// when
				ExtractableResponse<Response> response = 주문테이블_생성_요청(빈_테이블_입력);
				// then
				주문테이블_정상_생성됨(response);
			}),
			dynamicTest("채워진 테이블을 등록한다.", () -> {
				// when
				ExtractableResponse<Response> response = 주문테이블_생성_요청(채워진_테이블_입력);
				// then
				주문테이블_정상_생성됨(response);
			}),
			dynamicTest("채워진 테이블을 빈테이블로 변환할 수 있다.", () -> {
				// given
				Long 채워진_테이블_ID = 테이블_생성됨(2, false);
				// when
				ExtractableResponse<Response> response = 주문테이블_비움_상태_변경_요청(채워진_테이블_ID, true);
				// then
				주문테이블_빈테이블_정상_변경됨(response);
			}),
			dynamicTest("단체 지정이 되어있는 테이블의 비움 상태를 변경할 수 없다.", () -> {
				// given
				Long 빈_테이블_A_ID = 테이블_생성됨(2, true);
				Long 빈_테이블_B_ID = 테이블_생성됨(2, true);
				단체지정됨(빈_테이블_A_ID, 빈_테이블_B_ID);
				// when
				ExtractableResponse<Response> response = 주문테이블_비움_상태_변경_요청(빈_테이블_A_ID, false);
				// then
				요청_실패됨(response);
			}),
			dynamicTest("요리중이거나 식사중인 테이블의 비움 상태를 변경할 수 없다.", () -> {
				// given
				Long 채워진_테이블_ID = 테이블_생성됨(2, false);
				주문_생성됨(채워진_테이블_ID, 짜장_탕수_세트_생성됨());
				// when
				ExtractableResponse<Response> response = 주문테이블_비움_상태_변경_요청(채워진_테이블_ID, true);

				요청_실패됨(response);
			}),
			dynamicTest("테이블의 손님 수를 변경할 수 있다.", () -> {
				// given
				Long 채워진_테이블_ID = 테이블_생성됨(2, false);
				// when
				ExtractableResponse<Response> response = 주문테이블_손님_변경_요청(채워진_테이블_ID, 10);
				// then
				주문테이블_손님수_정상_변경됨(response, 10);
			}),
			dynamicTest("변경 요청 손님수가 0 미만이면 변경할 수 없다.", () -> {
				// given
				Long 채워진_테이블_ID = 테이블_생성됨(2, false);
				// when
				ExtractableResponse<Response> response = 주문테이블_손님_변경_요청(채워진_테이블_ID, -1);
				// then
				요청_실패됨(response);
			}),
			dynamicTest("빈 테이블의 손님수는 변경할 수 없다.", () -> {
				// given
				Long 빈_테이블_ID = 테이블_생성됨(2, true);
				// when
				ExtractableResponse<Response> response = 주문테이블_손님_변경_요청(빈_테이블_ID, 10);
				// then
				요청_실패됨(response);
			}),
			dynamicTest("테이블 목록을 조회한다.", () -> {
				// given
				Long 채워진_테이블_A_ID = 테이블_생성됨(2, false);
				Long 채워진_테이블_B_ID = 테이블_생성됨(2, false);
				// when
				ExtractableResponse<Response> response = 주문테이블_목록_조회_요청();
				// then
				주문테이블_목록_정상_조회됨(response, 채워진_테이블_A_ID, 채워진_테이블_B_ID);
			})
		);
	}

	public static Long 테이블_생성됨(int guestCounts, boolean empty) {
		OrderTableRequest orderTableRequest = OrderTableRequest.of(guestCounts, empty);
		return 주문테이블_생성_요청(orderTableRequest).as(OrderTableResponse.class).getId();
	}

	public static ExtractableResponse<Response> 주문테이블_생성_요청(OrderTableRequest orderTableRequest) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTableRequest)
			.when().post("/api/tables")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 주문테이블_목록_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get("/api/tables")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 주문테이블_비움_상태_변경_요청(Long id, boolean empty) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(empty)
			.when().put("/api/tables/{id}/empty", id)
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 주문테이블_손님_변경_요청(Long id, int guestCounts) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(guestCounts)
			.when().put("/api/tables/{id}/number-of-guests", id)
			.then().log().all()
			.extract();
	}

	private void 주문테이블_정상_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private void 요청_실패됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void 주문테이블_목록_정상_조회됨(ExtractableResponse<Response> response, Long... ID_목록) {
		List<Long> 조회_결과_ID_목록 = response.jsonPath()
			.getList(".", OrderTableResponse.class)
			.stream()
			.map(OrderTableResponse::getId)
			.collect(Collectors.toList());

		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(조회_결과_ID_목록).containsAll(Arrays.asList(ID_목록))
		);
	}

	private void 주문테이블_빈테이블_정상_변경됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertTrue(response.as(OrderTableResponse.class).isEmpty());
	}

	private void 주문테이블_손님수_정상_변경됨(ExtractableResponse<Response> response, int numberOfGuests) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertEquals(numberOfGuests, response.as(OrderTableResponse.class).getGuestCounts());
	}

}
