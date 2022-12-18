package kitchenpos.acceptance;

import static kitchenpos.acceptance.RestAssuredUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.groups.Tuple;
import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class TableGroupAcceptanceUtils {

	public static final String TABLE_GROUP_URL = "/api/table-groups";

	private TableGroupAcceptanceUtils() {
	}

	public static TableGroup 단체_지정_되어잇음(List<Long> orderTableIds) {
		return 단체_지정_요청(orderTableIds).as(TableGroup.class);
	}

	public static ExtractableResponse<Response> 단체_지정_요청(List<Long> orderTableIds) {
		return post(TABLE_GROUP_URL, tableGroupRequest(orderTableIds)).extract();
	}

	public static void 단체_지정_됨(ExtractableResponse<Response> response, List<Long> expectedOrderTableIds) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(response.as(TableGroup.class))
				.satisfies(group -> {
					assertThat(group.getId()).isNotNull();
					assertThat(group.getCreatedDate())
						.isEqualToIgnoringMinutes(LocalDateTime.now());
					assertThat(group.getOrderTables())
						.extracting(OrderTable::getId, OrderTable::getTableGroupId)
						.containsExactly(
							expectedOrderTableIds
								.stream()
								.map(id -> tuple(id, group.getId()))
								.toArray(Tuple[]::new)
						);
				})
		);
	}

	public static ExtractableResponse<Response> 단체_지정_해제_요청(long tableGroupId) {
		return delete(TABLE_GROUP_URL + "/" + tableGroupId).extract();
	}

	public static void 단체_지정_해제_됨(ExtractableResponse<Response> response) {
		List<OrderTable> orderTableResponse = TableAcceptanceUtils.주문_테이블_목록_조회_요청()
			.as(new TypeRef<List<OrderTable>>() {
			});
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
			() -> assertThat(orderTableResponse)
				.extracting(OrderTable::getTableGroupId)
				.containsExactly(null, null)
		);
	}

	private static TableGroup tableGroupRequest(List<Long> orderTableIds) {
		return new TableGroup(null, LocalDateTime.now(), orderTableIds.stream()
			.map(TableGroupAcceptanceUtils::orderTableRequest)
			.collect(Collectors.toList()));
	}

	private static OrderTable orderTableRequest(Long orderTableId) {
		return new OrderTable(orderTableId, null, 0, true);
	}
}
