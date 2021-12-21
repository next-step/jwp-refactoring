package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.order.OrderFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceTest extends AcceptanceTest {
  @DisplayName("테이블 그룹을 생성한다.")
  @Test
  void createTableGroup() {
    // given
    OrderTable 주문_테이블_첫번째 = TableAcceptanceTest.주문_테이블_생성됨(OrderFactory.ofOrderTable(true, 4));
    OrderTable 주문_테이블_두번째 = TableAcceptanceTest.주문_테이블_생성됨(OrderFactory.ofOrderTable(true, 5));

    TableGroup 테이블_그룹_첫번째 = OrderFactory.ofTableGroup(Arrays.asList(주문_테이블_첫번째, 주문_테이블_두번째));

    // when
    ExtractableResponse<Response> response = 테이블_그룹_생성_요청(테이블_그룹_첫번째);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  @DisplayName("테이블 그룹을 해제한다.")
  @Test
  void changeTableGroup() {
    // given
    OrderTable 주문_테이블_첫번째 = TableAcceptanceTest.주문_테이블_생성됨(OrderFactory.ofOrderTable(true, 4));
    OrderTable 주문_테이블_두번째 = TableAcceptanceTest.주문_테이블_생성됨(OrderFactory.ofOrderTable(true, 5));

    TableGroup 테이블_그룹_첫번째 = OrderFactory.ofTableGroup(Arrays.asList(주문_테이블_첫번째, 주문_테이블_두번째));
    테이블_그룹_첫번째 = 테이블_그룹_생성_요청(테이블_그룹_첫번째).as(TableGroup.class);

    // when
    ExtractableResponse<Response> response = 테이블_그룹_해제_요청(테이블_그룹_첫번째.getId());

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  private ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroup tableGroup) {
    return ofRequest(Method.POST, "/api/table-groups", tableGroup);
  }

  private ExtractableResponse<Response> 테이블_그룹_해제_요청(Long tableGroupId) {
    Map<String, Object> pathParams = new HashMap<>();
    pathParams.put("tableGroupId", tableGroupId);
    return ofRequest(Method.DELETE, "/api/table-groups/{tableGroupId}", pathParams);
  }
}
