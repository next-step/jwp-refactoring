package kitchenpos.table.application;

import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("테이블 관련 Service 기능 테스트")
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TableServiceTest {

    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        //given
        OrderTableRequest request = 테이블_요청_만들기(0, true);

        //when
        OrderTableResponse result = tableService.create(request);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isEqualTo(request.getTableGroupId());
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        assertThat(result.getEmpty()).isEqualTo(request.getEmpty());
    }

    @DisplayName("빈 테이블 여부를 업데이트 한다.")
    @Test
    void changeEmpty() {
        //given
        long requestTableId = 1L;
        OrderTable request = new OrderTable(null, null, 0, false);

        //whe
        OrderTable result = tableService.changeEmpty(requestTableId, request);

        //then
        assertThat(result.isEmpty()).isEqualTo(request.isEmpty());
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("단체 지정이 되어있는 경우 빈 테이블 여부 업데이트 할 수 없다.")
    @Test
    void changeEmpty_table_group() {
        //given
        OrderTable orderTable1 = new OrderTable(10L, 1L, 1, false);
        OrderTable orderTable2 = new OrderTable(11L, 2L, 2, false);
        tableGroupDao.save(new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2)));

        long requestTableId = 10;
        OrderTable request = new OrderTable(null, null, 0, true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(requestTableId, request));
    }

    @DisplayName("방문 손님 수를 업데이트 한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        long requestTableId = 1;
        OrderTable request = new OrderTable(null, null, 10, true);

        //when
        OrderTable result = tableService.changeNumberOfGuests(requestTableId, request);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("방문 손님 수가 0명 미만인 경우 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_less_than_zero() {
        //given
        long requestTableId = 1;
        OrderTable request = new OrderTable(null, null, -1, true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request));
    }

    @DisplayName("빈 테이블인 경우 방문 손님 수를 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_empty_table() {
        //given
        long requestTableId = 3;
        OrderTable request = new OrderTable(null, null, 10, true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request));
    }


}
