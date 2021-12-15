package kitchenpos.acceptance.step;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.function.Consumer;

import static kitchenpos.utils.RestAssuredUtil.get;
import static kitchenpos.utils.RestAssuredUtil.post;
import static kitchenpos.utils.RestAssuredUtil.put;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableAcceptStep {
    private static final String BASE_URL = "/api/tables";

    public static ExtractableResponse<Response> 테이블_등록_요청(OrderTable 등록_요청_데이터) {
        return post(BASE_URL, 등록_요청_데이터);
    }

    public static OrderTable 테이블이_등록되어_있음(int numberOfGuests, boolean empty) {
        OrderTable 등록_요청_데이터 = new OrderTable();
        등록_요청_데이터.setNumberOfGuests(numberOfGuests);
        등록_요청_데이터.setEmpty(empty);

        return 테이블_등록_요청(등록_요청_데이터).as(OrderTable.class);
    }

    public static OrderTable 테이블_등록_확인(ExtractableResponse<Response> 테이블_등록_응답, OrderTable 등록_요청_데이터) {
        OrderTable 등록된_테이블 = 테이블_등록_응답.as(OrderTable.class);

        assertAll(
                () -> assertThat(테이블_등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(테이블_등록_응답.header("Location")).isNotBlank(),
                () -> assertThat(등록된_테이블).satisfies(등록된_메뉴_확인(등록_요청_데이터))
        );

        return 등록된_테이블;
    }

    private static Consumer<OrderTable> 등록된_메뉴_확인(OrderTable 등록_요청_데이터) {
        return orderTable -> {
            assertThat(orderTable.getId()).isNotNull();
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(등록_요청_데이터.getNumberOfGuests());
            assertThat(orderTable.isEmpty()).isEqualTo(등록_요청_데이터.isEmpty());
        };
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return get(BASE_URL);
    }

    public static void 테이블_목록_조회_확인(ExtractableResponse<Response> 테이블_목록_조회_응답, OrderTable 등록된_테이블) {
        List<OrderTable> 조회된_테이블_목록 = 테이블_목록_조회_응답.as(new TypeRef<List<OrderTable>>() {
        });

        assertAll(
                () -> assertThat(테이블_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회된_테이블_목록).satisfies(조회된_테이블_목록_확인(등록된_테이블))
        );
    }

    private static Consumer<List<? extends OrderTable>> 조회된_테이블_목록_확인(OrderTable 등록된_테이블) {
        return orderTables -> {
            assertThat(orderTables.size()).isOne();
            assertThat(orderTables).first()
                    .satisfies(orderTable -> {
                        assertThat(orderTable.getId()).isEqualTo(등록된_테이블.getId());
                        assertThat(orderTable.getNumberOfGuests()).isEqualTo(등록된_테이블.getNumberOfGuests());
                        assertThat(orderTable.isEmpty()).isEqualTo(등록된_테이블.isEmpty());
                    });
        };
    }

    public static ExtractableResponse<Response> 테이블_상태_변경_요청(ExtractableResponse<Response> 테이블_등록_응답, OrderTable 상태_변경_요청_데이터) {
        String uri = 테이블_등록_응답.header("Location");

        return put(uri + "/empty", 상태_변경_요청_데이터);
    }

    public static void 테이블_상태_확인(ExtractableResponse<Response> 테이블_상태_변경_응답, OrderTable 상태_변경_요청_데이터) {
        OrderTable 변경된_테이블 = 테이블_상태_변경_응답.as(OrderTable.class);

        assertAll(
                () -> assertThat(테이블_상태_변경_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(변경된_테이블).satisfies(변경된_테이블_상태_확인(상태_변경_요청_데이터))
        );
    }

    private static Consumer<OrderTable> 변경된_테이블_상태_확인(OrderTable 상태_변경_요청_데이터) {
        return orderTable -> assertThat(orderTable.isEmpty()).isEqualTo(상태_변경_요청_데이터.isEmpty());
    }

    public static ExtractableResponse<Response> 테이블_인원_변경_요청(ExtractableResponse<Response> 테이블_인원_변경_응답, OrderTable 인원_변경_요청_데이터) {
        String uri = 테이블_인원_변경_응답.header("Location");

        return put(uri + "/number-of-guests", 인원_변경_요청_데이터);
    }

    public static void 테이블_인원_확인(ExtractableResponse<Response> 테이블_상태_변경_응답, OrderTable 인원_변경_요청_데이터) {
        OrderTable 변경된_테이블 = 테이블_상태_변경_응답.as(OrderTable.class);

        assertAll(
                () -> assertThat(테이블_상태_변경_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(변경된_테이블).satisfies(변경된_테이블_인원_확인(인원_변경_요청_데이터))
        );
    }

    private static Consumer<OrderTable> 변경된_테이블_인원_확인(OrderTable 인원_변경_요청_데이터) {
        return orderTable -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(인원_변경_요청_데이터.getNumberOfGuests());
    }
}
