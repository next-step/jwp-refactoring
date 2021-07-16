package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 그룹 등록 및 그룹 해제 시나리오")
	@Test
	void createTableGroupAndUngroupScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> tableWithFivePeopleCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(5, true))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTableWithFivePeople = tableWithFivePeopleCreatedResponse.as(OrderTable.class);

		ExtractableResponse<Response> tableWithTenPeopleCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTable(10, true))
			.when().post("/api/tables")
			.then().log().all()
			.extract();
		OrderTable createdOrderTableWithTenPeople = tableWithTenPeopleCreatedResponse.as(OrderTable.class);

		// Scenario
		// When
		ExtractableResponse<Response> tableGroupCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithTenPeople)))
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
		TableGroup createdTableGroup = tableGroupCreatedResponse.as(TableGroup.class);
		// Then
		assertThat(tableGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdTableGroup.getOrderTables().size()).isEqualTo(2);

		// When
		ExtractableResponse<Response> ungroupedTableGroupResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TableGroup(LocalDateTime.now(), Arrays.asList(createdOrderTableWithFivePeople, createdOrderTableWithTenPeople)))
			.when().delete("/api/table-groups/" + createdTableGroup.getId())
			.then().log().all()
			.extract();
		// Then
		assertThat(ungroupedTableGroupResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
