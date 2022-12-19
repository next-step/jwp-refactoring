package kitchenpos.table.ordertable.ui;

import io.restassured.RestAssured;
import kitchenpos.TableUiTest;
import kitchenpos.table.ordertable.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static kitchenpos.table.ordertable.domain.OrderTableTestFixture.두_명의_방문객이_존재하는_테이블;
import static kitchenpos.table.ordertable.domain.OrderTableTestFixture.빈_상태;
import static kitchenpos.table.ordertable.domain.OrderTableTestFixture.빈_테이블;
import static kitchenpos.table.ordertable.domain.OrderTableTestFixture.한_명의_방문객;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 ui 테스트")
public
class TableRestControllerTest extends TableUiTest {

    private OrderTable 주문_테이블;

    @BeforeEach
    void setup() {
        super.setUp();
        주문_테이블 = 빈_테이블();
    }

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //when:
        final OrderTable 저장된_주문_테이블 = 주문_테이블_생성_됨(주문_테이블);
        //then:
        assertThat(저장된_주문_테이블).isEqualTo(주문_테이블);
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() {
        //when:
        주문_테이블_생성_됨(주문_테이블);

        final List<OrderTable> 주문_테이블_목록 = 주문_테이블_목록_조회_됨();
        //then:
        assertThat(주문_테이블_목록).isNotEmpty();
    }

    @DisplayName("빈 주문 테이블 변경 성공")
    @Test
    void 빈_주문_테이블_변경_성공() {
        //given:
        final OrderTable 저장된_주문_테이블 = 주문_테이블_생성_됨(두_명의_방문객이_존재하는_테이블());

        저장된_주문_테이블.changeEmpty(빈_상태);
        //when:
        final OrderTable 빈_주문_테이블 = 주문_테이블_상태_변경_됨(저장된_주문_테이블);
        //then:
        assertThat(빈_주문_테이블.isEmpty()).isTrue();
    }

    @DisplayName("방문 손님 수 변경 성공")
    @Test
    void 방문_손님_수_변경_성공() {
        //given:
        final OrderTable 저장된_주문_테이블 = 주문_테이블_생성_됨(두_명의_방문객이_존재하는_테이블());
        저장된_주문_테이블.changeNumberOfGuest(한_명의_방문객);
        //when:
        final OrderTable 빈_주문_테이블 = 방문_손님_수_변경_됨(저장된_주문_테이블);
        //then:
        assertThat(빈_주문_테이블.getNumberOfGuests()).isEqualTo(한_명의_방문객);
    }

    public static OrderTable 주문_테이블_생성_됨(OrderTable 주문_테이블) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(주문_테이블)
                .when().post("/api/tables")
                .then().log().all()
                .extract().as(OrderTable.class);
    }

    private List<OrderTable> 주문_테이블_목록_조회_됨() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract().jsonPath().getList(".", OrderTable.class);
    }

    private OrderTable 주문_테이블_상태_변경_됨(OrderTable 저장된_주문_테이블) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(저장된_주문_테이블)
                .when().put("/api/tables/{orderTableId}/empty", 저장된_주문_테이블.getId())
                .then().log().all()
                .extract().as(OrderTable.class);
    }

    private OrderTable 방문_손님_수_변경_됨(OrderTable 저장된_주문_테이블) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(저장된_주문_테이블)
                .when().put("/api/tables/{orderTableId}/number-of-guests", 저장된_주문_테이블.getId())
                .then().log().all()
                .extract().as(OrderTable.class);
    }
}
