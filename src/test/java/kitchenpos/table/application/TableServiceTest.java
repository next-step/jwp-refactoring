package kitchenpos.table.application;

import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
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
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
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
        OrderTableRequest request = 테이블_요청_만들기(0, false);

        //when
        OrderTableResponse result = tableService.changeEmpty(requestTableId, request);

        //then
        assertThat(result.getEmpty()).isEqualTo(request.getEmpty());
    }

    @DisplayName("단체 지정이 되어있는 경우 빈 테이블 여부 업데이트 할 수 없다.")
    @Test
    void changeEmpty_table_group() {
        //given
        kitchenpos.table.domain.OrderTable orderTable1 = 테이블_만들기(0, true);
        kitchenpos.table.domain.OrderTable orderTable2 = 테이블_만들기(0, false);
        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now());
        tableGroup.addOrderTable(orderTable1);
        tableGroup.addOrderTable(orderTable2);
        tableGroupRepository.save(tableGroup);

        OrderTableRequest request = 테이블_요청_만들기(0, false);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), request));
    }

    @DisplayName("방문 손님 수를 업데이트 한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        kitchenpos.table.domain.OrderTable orderTable = 테이블_만들기(3, false);
        orderTableRepository.save(orderTable);
        long requestTableId = orderTable.getId();
        OrderTableRequest request = 테이블_요청_만들기(3);

        //when
        OrderTableResponse result = tableService.changeNumberOfGuests(requestTableId, request);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("방문 손님 수가 0명 미만인 경우 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_less_than_zero() {
        //given
        kitchenpos.table.domain.OrderTable orderTable = 테이블_만들기(3, false);
        orderTableRepository.save(orderTable);
        long requestTableId = orderTable.getId();
        OrderTableRequest request = 테이블_요청_만들기(-1);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request))
                .withMessageContaining("방문 손님 수는 0명 미만으로 변경할 수 없습니다");
    }

    @DisplayName("빈 테이블인 경우 방문 손님 수를 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_empty_table() {
        //given
        kitchenpos.table.domain.OrderTable orderTable = 테이블_만들기(3, true);
        orderTableRepository.save(orderTable);
        long requestTableId = orderTable.getId();
        OrderTableRequest request = 테이블_요청_만들기(5);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request));
    }


}
