package kitchenpos.tablegroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.TableGroup;
import kitchenpos.ordertable.acceptance.OrderTableRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private TableGroup tableGroupA;

    @BeforeEach
    public void setUp() {
        super.setUp();

        tableGroupA = TableGroupRestAssured.from(1L
                , LocalDateTime.now()
                ,  Arrays.asList(OrderTableRestAssured.from(1L, 0, true), OrderTableRestAssured.from(2L, 0, true)));
    }

    @DisplayName("테이블군 생성 요청")
    @Test
    void createOrderTable() {
        ExtractableResponse<Response> response = TableGroupRestAssured.테이블군_생성_요청(tableGroupA);

        TableGroupRestAssured.테이블군_생성됨(response);
    }

    @DisplayName("테이블군 생성 예외 - ordertable size < 2 ")
    @Test
    void createOrderTable_sizeException() {
        TableGroup tableGroup = TableGroupRestAssured.from(1L
                , LocalDateTime.now()
                ,  Arrays.asList(OrderTableRestAssured.from(1L, 0, true)));

        ExtractableResponse<Response> response = TableGroupRestAssured.테이블군_생성_요청(tableGroup);

        TableGroupRestAssured.테이블군_생성_안됨(response);
    }

    @DisplayName("테이블군 삭제")
    @TestFactory
    Stream<DynamicTest> deleteTableGroup() {
        return Stream.of(
                DynamicTest.dynamicTest("테이블군 생성",() -> {
                    tableGroupA = TableGroupRestAssured.from(1L
                            , LocalDateTime.now()
                            ,  Arrays.asList(OrderTableRestAssured.from(1L, 0, true), OrderTableRestAssured.from(2L, 0, true)));
                    ExtractableResponse<Response> response = TableGroupRestAssured.테이블군_생성_요청(tableGroupA);

                    TableGroupRestAssured.테이블군_생성됨(response);
                }),
                DynamicTest.dynamicTest("테이블군 삭제",() -> {
                    ExtractableResponse<Response> response = TableGroupRestAssured.테이블군_삭제_요청(1L);
                    TableGroupRestAssured.테이블군_삭제됨(response);
                })
        );
    }
}
