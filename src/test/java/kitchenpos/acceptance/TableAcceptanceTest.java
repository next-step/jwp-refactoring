package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;

public class TableAcceptanceTest extends AcceptanceTest {
	@DisplayName("주문 테이블 등록, 인원수 조정, 빈테이블로 변경 및 조회 시나리오")
	@Test
	void createOrderTableAndChangeNumberOfGuestAndChangeEmptyAndFindScenario() {
		// Scenario
		// When : 주문 테이블 등록
		ExtractableResponse<Response> tableCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(10, false))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTable = tableCreatedResponse.as(OrderTable.class);

		// Then
		assertThat(tableCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(10);

		// When : 인원수 조정
		ExtractableResponse<Response> changeNumberResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(5))
			.when().put("/api/tables/" + createdOrderTable.getId() + "/number-of-guests")
			.then().log().all()
			.extract();
		OrderTable changeNumberOrderTable = changeNumberResponse.as(OrderTable.class);

		// Then
		assertThat(changeNumberResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeNumberOrderTable.getNumberOfGuests()).isEqualTo(5);

		// When : 빈 테이블로 변경
		ExtractableResponse<Response> changeEmptyResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(true))
			.when().put("/api/tables/" + createdOrderTable.getId() + "/empty")
			.then().log().all()
			.extract();
		OrderTable changeEmptyOrderTable = changeEmptyResponse.as(OrderTable.class);

		// Then
		assertThat(changeNumberResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeEmptyOrderTable.isEmpty()).isEqualTo(true);

		// When : 주문 테이블 조회
		ExtractableResponse<Response> findOrderTableResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/tables")
			.then().log().all()
			.extract();

		boolean idOrderTableEmpty = findOrderTableResponse.jsonPath().getList(".", OrderTable.class).stream()
			.filter(orderTable -> orderTable.getId() == createdOrderTable.getId())
			.map(OrderTable::isEmpty)
			.findFirst()
			.get()
			;

		// Then
		assertThat(findOrderTableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(idOrderTableEmpty).isEqualTo(true);
	}
}
