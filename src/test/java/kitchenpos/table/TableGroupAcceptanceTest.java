package kitchenpos.table;

import static kitchenpos.table.TableAcceptanceAPI.단체_손님을_생성;
import static kitchenpos.table.TableAcceptanceAPI.단체_손님을_해제;
import static kitchenpos.table.TableAcceptanceAPI.손님_입장;
import static kitchenpos.table.TableAcceptanceTest.빈자리;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableGroupAcceptanceTest extends AcceptanceTest {

    OrderTable 손님1;
    OrderTable 손님2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        손님1 = 손님_입장(4, 빈자리).as(OrderTable.class);
        손님2 = 손님_입장(4, 빈자리).as(OrderTable.class);
    }

    @Test
    @DisplayName("단체 손님을 생성한다")
    void 단체_손님을_생성한다() {
        // when
        ExtractableResponse<Response> response = 단체_손님을_생성(Arrays.asList(손님1, 손님2));

        // then
        단체_손님이_생성됨(response);
    }

    @Test
    @DisplayName("단체 손님을 해제한다")
    void 단체_손님을_해제한다() {
        // given
        Long 단체손님 = 단체_손님을_생성(Arrays.asList(손님1, 손님2)).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 단체_손님을_해제(단체손님);

        // then
        단체_손님이_해제됨(response);
    }

    public static void 단체_손님이_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 단체_손님이_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
