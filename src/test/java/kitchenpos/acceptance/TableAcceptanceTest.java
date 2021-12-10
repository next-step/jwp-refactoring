package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.TableAcceptanceStep.테이블_목록_조회_요청;
import static kitchenpos.acceptance.step.TableAcceptanceStep.테이블_목록_조회됨;
import static kitchenpos.acceptance.step.TableAcceptanceStep.테이블_빈_상태_수정_요청;
import static kitchenpos.acceptance.step.TableAcceptanceStep.테이블_빈_상태_수정됨;
import static kitchenpos.acceptance.step.TableAcceptanceStep.테이블_생성_요청;
import static kitchenpos.acceptance.step.TableAcceptanceStep.테이블_생성됨;
import static kitchenpos.acceptance.step.TableAcceptanceStep.테이블_손님_수_수정_요청;
import static kitchenpos.acceptance.step.TableAcceptanceStep.테이블_손님_수_수정됨;
import static kitchenpos.acceptance.step.TableAcceptanceStep.테이블_저장되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 관련 기능")
class TableAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테이블을 등록할 수 있다.")
    void create() {
        //given
        final int numberOfGuests = 5;
        final boolean empty = true;

        //when
        ExtractableResponse<Response> response = 테이블_생성_요청(numberOfGuests, empty);

        //then
        테이블_생성됨(response, numberOfGuests, empty);
    }

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    void list() {
        //given
        final int numberOfGuests = 5;
        final boolean empty = true;
        테이블_저장되어_있음(numberOfGuests, empty);

        //when
        ExtractableResponse<Response> response = 테이블_목록_조회_요청();

        //then
        테이블_목록_조회됨(response, numberOfGuests, empty);
    }

    @Test
    @DisplayName("빈 테이블 여부 상태를 변경할 수 있다.")
    void changeEmpty() {
        //given
        final boolean expectedEmpty = false;
        OrderTable orderTable = 테이블_저장되어_있음(5, true);

        //when
        ExtractableResponse<Response> response = 테이블_빈_상태_수정_요청(orderTable.getId(), expectedEmpty);

        //then
        테이블_빈_상태_수정됨(response, expectedEmpty);
    }

    @Test
    @DisplayName("테이블의 방문한 손님 수 상태를 변경할 수 있다.")
    void changeNumberOfGuests() {
        //given
        final int expectedNumber = 1;
        OrderTable orderTable = 테이블_저장되어_있음(5, false);

        //when
        ExtractableResponse<Response> response = 테이블_손님_수_수정_요청(orderTable.getId(), expectedNumber);

        //then
        테이블_손님_수_수정됨(response, expectedNumber);
    }
}
