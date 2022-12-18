package kitchenpos.acceptance;

import static kitchenpos.acceptance.RestAssuredUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.OrderTable;

public class TableAcceptanceUtils {

	private static final String TABLE_API_URL = "/api/tables";

	private TableAcceptanceUtils() {
	}

	public static OrderTable 주문_테이블_등록_되어_있음(int numberOfGuests, boolean empty) {
		return 주문_테이블_생성_요청(numberOfGuests, empty).as(OrderTable.class);
	}

	public static ExtractableResponse<Response> 주문_테이블_생성_요청(int numberOfGuests, boolean empty) {
		return post(TABLE_API_URL, createRequest(numberOfGuests, empty)).extract();
	}

	public static void 주문_테이블_생성됨(ExtractableResponse<Response> response,
		int expectedNumberOfGuests, boolean expectedEmpty) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(response.as(OrderTable.class))
				.extracting(
					OrderTable::getNumberOfGuests, OrderTable::isEmpty, OrderTable::getTableGroupId)
				.containsExactly(expectedNumberOfGuests, expectedEmpty, null)
		);
	}

	public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
		return get(TABLE_API_URL).extract();
	}

	public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response,
		int expectedNumberOfGuests, boolean expectedEmpty) {
		List<OrderTable> orderTables = response.as(new TypeRef<List<OrderTable>>() {
		});
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(orderTables)
				.first()
				.extracting(OrderTable::getNumberOfGuests, OrderTable::isEmpty)
				.containsExactly(expectedNumberOfGuests, expectedEmpty)
		);
	}

	public static ExtractableResponse<Response> 빈_테이블로_수정_요청(long id, boolean empty) {
		return put(TABLE_API_URL + "/" + id + "/empty", updateEmptyRequest(empty)).extract();
	}

	public static void 빈_테이블로_수정됨(ExtractableResponse<Response> response, boolean expectedEmpty) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.as(OrderTable.class).isEmpty()).isEqualTo(expectedEmpty)
		);
	}

	public static ExtractableResponse<Response> 손님_수_수정_요청(long id, int numberOfGuests) {
		return put(TABLE_API_URL + "/" + id + "/number-of-guests", updateNumbersRequest(numberOfGuests)).extract();
	}

	private static OrderTable updateEmptyRequest(boolean empty) {
		return new OrderTable(null, null, 0, empty);
	}

	private static OrderTable updateNumbersRequest(int numberOfGuests) {
		return new OrderTable(null, null, numberOfGuests, false);
	}

	public static void 손님_수_수정됨(ExtractableResponse<Response> response, int expectedNumberOfGuests) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(expectedNumberOfGuests)
		);
	}

	private static OrderTable createRequest(int numberOfGuests, boolean empty) {
		return new OrderTable(null, null, numberOfGuests, empty);
	}
}
