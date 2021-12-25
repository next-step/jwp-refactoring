package kitchenpos.ordertable.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import kitchenpos.ordertable.testfixtures.acceptance.TableAcceptanceFixtures;
import kitchenpos.ordertable.testfixtures.acceptance.TableGroupAcceptanceFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 첫번째_테이블;
    private OrderTableResponse 두번째_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        //background
        첫번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(6, true)).getBody();
        두번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(3, true)).getBody();
    }

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() {
        //given
        TableGroupRequest tableGroupRequest = TableGroupAcceptanceFixtures.테이블_그룹_정의(Arrays.asList(
            TableAcceptanceFixtures.테이블_정의(첫번째_테이블.getId()),
            TableAcceptanceFixtures.테이블_정의(두번째_테이블.getId())
        ));

        //when
        ResponseEntity<TableGroupResponse> 생성_결과 = TableGroupAcceptanceFixtures.테이블_그룹_생성(
            tableGroupRequest);

        //then
        그룹생성_정상_확인(생성_결과);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        //given
        TableGroupRequest tableGroupRequest = TableGroupAcceptanceFixtures.테이블_그룹_정의(Arrays.asList(
            TableAcceptanceFixtures.테이블_정의(첫번째_테이블.getId()),
            TableAcceptanceFixtures.테이블_정의(두번째_테이블.getId())
        ));
        TableGroupResponse 테이블_그룹_생성됨 = TableGroupAcceptanceFixtures.테이블_그룹_생성(
            tableGroupRequest).getBody();

        //when
        ResponseEntity 그룹_해제_결과 = TableGroupAcceptanceFixtures.테이블_그룹_해제(테이블_그룹_생성됨.getId());

        //then
        그룹해제_정상_확인(그룹_해제_결과);
    }

    private void 그룹생성_정상_확인(ResponseEntity<TableGroupResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private void 그룹해제_정상_확인(ResponseEntity response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
