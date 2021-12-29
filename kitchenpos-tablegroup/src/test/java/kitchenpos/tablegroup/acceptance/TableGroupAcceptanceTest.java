package kitchenpos.tablegroup.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.ordertable.TableAcceptanceFixtures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import kitchenpos.tablegroup.TableGroupAcceptanceFixtures;

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
        주문테이블_그룹핑_확인(Arrays.asList(첫번째_테이블, 두번째_테이블));
    }

    @DisplayName("하나의 테이블로만 그룹 생성 오류")
    @Test
    void create_exception() {
        //given
        TableGroupRequest tableGroupRequest = TableGroupAcceptanceFixtures.테이블_그룹_정의(Arrays.asList(
            TableAcceptanceFixtures.테이블_정의(첫번째_테이블.getId())));

        //when
        ResponseEntity<TableGroupResponse> 생성_결과 = TableGroupAcceptanceFixtures.테이블_그룹_생성(
            tableGroupRequest);

        //then
        그룹생성_오류(생성_결과);
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
        주문테이블_그룹해제_확인(Arrays.asList(첫번째_테이블, 두번째_테이블));
    }

    private void 그룹생성_정상_확인(ResponseEntity<TableGroupResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private void 주문테이블_그룹핑_확인(List<OrderTableResponse> expectedTables) {
        List<Long> expectedTableIds = expectedTables.stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        List<OrderTableResponse> 주문테이블_조회됨 = TableAcceptanceFixtures.테이블_전체_조회_요청().getBody();
        주문테이블_조회됨.stream()
            .filter(tableResponse -> expectedTableIds.contains(tableResponse.getId()))
            .forEach(tableResponse -> assertThat(tableResponse.getTableGroupId()).isNotNull());
    }

    private void 그룹해제_정상_확인(ResponseEntity response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void 주문테이블_그룹해제_확인(List<OrderTableResponse> expectedTables) {
        List<Long> expectedTableIds = expectedTables.stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        List<OrderTableResponse> 주문테이블_조회됨 = TableAcceptanceFixtures.테이블_전체_조회_요청().getBody();
        주문테이블_조회됨.stream()
            .filter(tableResponse -> expectedTableIds.contains(tableResponse.getId()))
            .forEach(tableResponse -> assertThat(tableResponse.getTableGroupId()).isNull());
    }

    private void 그룹생성_오류(ResponseEntity<TableGroupResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
