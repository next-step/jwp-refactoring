package kitchenpos.table.tablegroup.ui;

import io.restassured.RestAssured;
import kitchenpos.TableUiTest;
import kitchenpos.table.tablegroup.dto.CreateTableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.ordertable.domain.OrderTableTestFixture.두_명의_방문객이_존재하는_테이블;
import static kitchenpos.table.ordertable.ui.TableRestControllerTest.주문_테이블_생성_됨;

@DisplayName("단체 지정 ui 테스트")
class TableGroupRestControllerTest extends TableUiTest {

    List<Long> 주문_테이블_id_목록;

    @BeforeEach
    void setup() {
        super.setUp();
        주문_테이블_id_목록 = Arrays.asList(
                주문_테이블_생성_됨(두_명의_방문객이_존재하는_테이블()).getId(),
                주문_테이블_생성_됨(두_명의_방문객이_존재하는_테이블()).getId());
    }

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //when, then:
        단체_지정_됨(주문_테이블_id_목록);
    }

    private void 단체_지정_됨(List<Long> 주문_테이블_id_목록) {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(new CreateTableGroupRequest(주문_테이블_id_목록))
                .when().post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
