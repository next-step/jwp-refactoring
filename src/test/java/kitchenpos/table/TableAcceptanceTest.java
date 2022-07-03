package kitchenpos.table;

import static kitchenpos.table.TableAcceptanceAPI.손님_리스트_조회;
import static kitchenpos.table.TableAcceptanceAPI.손님_입장;
import static kitchenpos.table.TableAcceptanceAPI.테이블_상태_변경_요청;
import static kitchenpos.table.TableAcceptanceAPI.테이블_손님수_변경_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableAcceptanceTest extends AcceptanceTest {

    OrderTable 주문테이블;
    public static boolean 빈자리 = true;
    public static boolean 사용중 = false;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("손님이 입장하여 빈 자리에 앉는다")
    void 손님이_입장하여_빈_자리에_앉는다() {
        // when
        ExtractableResponse<Response> response = 손님_입장(0, 빈자리);

        // then
        빈자리_착석_완료(response);
    }

    @Test
    @DisplayName("현재 존재하는 손님 리스트를 조회한다")
    void 현재_존재하는_손님_리스트를_조회한다() {
        // given
        손님_입장(0, 빈자리);

        // when
        ExtractableResponse<Response> response = 손님_리스트_조회();

        // then
        assertThat(response.jsonPath().getList("name")).hasSize(1);
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다")
    void 테이블_상태_변경() {
        // given
        주문테이블 = 손님_입장(0, 빈자리).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = 테이블_상태_변경_요청(주문테이블, 사용중);

        // then
        assertThat(response.jsonPath().getBoolean("empty")).isEqualTo(사용중);
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경한다")
    void 테이블의_손님_수를_변경한다() {
        // given
        주문테이블 = 손님_입장(0, 빈자리).as(OrderTable.class);
        테이블_상태_변경_요청(주문테이블, 사용중);

        // when
        ExtractableResponse<Response> response = 테이블_손님수_변경_요청(주문테이블, 5);

        // then
        assertThat(response.jsonPath().getInt("numberOfGuests")).isEqualTo(주문테이블.getNumberOfGuests());
    }

    public static void 빈자리_착석_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
