package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_목록_조회_요청;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_목록_조회됨;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_목록_포함됨;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_생성_요청;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_생성되어_있음;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_생성됨;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_이용객_수_변경_요청;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_이용객_수_변경됨;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_이용여부_변경_요청;
import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_이용여부_변경됨;
import static kitchenpos.domain.OrderTableFixture.createTable;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableAcceptanceTest extends AcceptanceTest{
    private OrderTable 주문테이블_A;
    private OrderTable 주문테이블_B;

    @BeforeEach
    void tableSetUp() {
        super.setUp();

        주문테이블_A = createTable(5, false);
        주문테이블_B = createTable(6, false);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createOrderTable() {
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(주문테이블_A);

        주문_테이블_생성됨(response);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void getOrderTableList() {
        ExtractableResponse<Response> createResponse1 = 주문_테이블_생성되어_있음(주문테이블_A);
        ExtractableResponse<Response> createResponse2 = 주문_테이블_생성되어_있음(주문테이블_B);

        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        주문_테이블_목록_조회_요청();

        주문_테이블_목록_조회됨(response);
        주문_테이블_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("주문 테이블 이용여부를 변경할 수 있다.")
    @Test
    void updateOrderTableEmpty() {
        boolean expectedEmpty = true;
        OrderTable existTable = 주문_테이블_생성되어_있음(this.주문테이블_A).as(OrderTable.class);
        OrderTable updateTable = createTable(existTable.getId(), null, existTable.getNumberOfGuests(), expectedEmpty);

        ExtractableResponse<Response> response = 주문_테이블_이용여부_변경_요청(updateTable);

        주문_테이블_이용여부_변경됨(response, expectedEmpty);
    }

    @DisplayName("주문 테이블 이용객 수를 변경할 수 있다.")
    @Test
    void updateOrderTableNumberOfGuests() {
        int expectedNumberOfGuests = 3;
        OrderTable existTable = 주문_테이블_생성되어_있음(this.주문테이블_A).as(OrderTable.class);
        OrderTable updateTable = createTable(existTable.getId(), null, expectedNumberOfGuests, existTable.isEmpty());

        ExtractableResponse<Response> response = 주문_테이블_이용객_수_변경_요청(updateTable);

        주문_테이블_이용객_수_변경됨(response, expectedNumberOfGuests);
    }
}
