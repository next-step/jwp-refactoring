package kitchenpos.ordertable;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.common.TableStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.menugroup.MenuGroupRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.stream.Stream;

public class OrderTableAcceptanceTest extends AcceptanceTest {

    private OrderTable tableA;
    private OrderTable tableA_A;

    @BeforeEach
    public void setUp() {
        super.setUp();

        tableA = OrderTableRestAssured.from(9L, 0, TableStatus.EMPTY.isTableEmpty());
        tableA_A = OrderTableRestAssured.from(8L, 5, TableStatus.USING.isTableEmpty());
    }

    @DisplayName("테이블 생성 요청")
    @Test
    void createOrderTable() {
        ExtractableResponse<Response> response = OrderTableRestAssured.테이블_생성_요청(tableA);

        OrderTableRestAssured.테이블_생성됨(response);
    }

    @DisplayName("테이블 정보 수정 요청 - empty 여부")
    @Test
    void modifyOrderTable_empty() {
        ExtractableResponse<Response> response = OrderTableRestAssured.테이블_empty_수정_요청(8L, tableA_A);
        OrderTableRestAssured.테이블_정보_수정됨(response);
    }

    @DisplayName("테이블 정보 수정 요청 예외 - 등록되지 않은 table인 경우")
    @Test
    void makeExceptionOrderTableWhenModifyIsEmptyData() {
        ExtractableResponse<Response> response = OrderTableRestAssured.테이블_empty_수정_요청(9L, tableA_A);
        OrderTableRestAssured.테이블_정보_수정안됨(response);
    }

    @DisplayName("테이블 정보 수정 요청 - 손님숫자")
    @TestFactory
    Stream<DynamicTest> modifyOrderTable_numberOfGuests() {
        return Stream.of(
                DynamicTest.dynamicTest("empty 여부 using으로 수정", () -> {
                    ExtractableResponse<Response> response = OrderTableRestAssured.테이블_empty_수정_요청(8L, tableA_A);
                    OrderTableRestAssured.테이블_정보_수정됨(response);
                }),
                DynamicTest.dynamicTest("손님숫자 수정", () -> {
                    ExtractableResponse<Response> response = OrderTableRestAssured.테이블_손님수_수정_요청(8L, tableA_A);
                    OrderTableRestAssured.테이블_정보_수정됨(response);
                })
        );
    }

    @DisplayName("테이블 손님숫자 정보 수정 요청 예외 - 등록되지 않은 table인 경우")
    @Test
    void makeExceptionOrderTableWhenModifyTableNumberOfGuests_table() {
        ExtractableResponse<Response> response = OrderTableRestAssured.테이블_empty_수정_요청(9L, tableA_A);
        OrderTableRestAssured.테이블_정보_수정안됨(response);
    }

    @DisplayName("테이블 손님숫자 정보 수정 요청 예외 - empty 인 경우")
    @Test
    void makeExceptionOrderTableWhenModifyTableNumberOfGuests_empty() {
        ExtractableResponse<Response> response = OrderTableRestAssured.테이블_empty_수정_요청(9L, tableA_A);
        OrderTableRestAssured.테이블_정보_수정안됨(response);
    }

    @DisplayName("테이블 조회 확인")
    @Test
    void showTableTest() {
        ExtractableResponse<Response> response = OrderTableRestAssured.테이블_조회_요청();

        OrderTableRestAssured.테이블_조회_목록_응답됨(response);
        OrderTableRestAssured.테이블_조회_목록_포함됨(response, Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L));
    }

}
