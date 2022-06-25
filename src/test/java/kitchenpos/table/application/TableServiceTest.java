package kitchenpos.table.application;

import static kitchenpos.helper.TableFixtures.빈_테이블_요청;
import static kitchenpos.helper.TableFixtures.주문_테이블;
import static kitchenpos.helper.TableFixtures.주문_테이블_요청;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static kitchenpos.helper.TableGroupFixtures.테이블_그룹_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("테이블 관련 Service 기능 테스트")
@DataJpaTest
@Import({TableService.class})
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
        OrderTableRequest request = 빈_테이블_요청;

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
        OrderTableRequest request = 주문_테이블_요청;

        //when
        OrderTableResponse result = tableService.changeEmpty(requestTableId, request);

        //then
        assertThat(result.getEmpty()).isEqualTo(request.getEmpty());
    }

    @DisplayName("단체 지정이 되어있는 경우 빈 테이블 여부 업데이트 할 수 없다.")
    @Test
    void changeEmpty_table_group() {
        //given
        OrderTable orderTable1 = 테이블_만들기(0, true);
        OrderTable orderTable2 = 테이블_만들기(3, false);
        TableGroup tableGroup = 테이블_그룹_만들기(null);
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
        OrderTable orderTable = 테이블_만들기(3, false);
        OrderTable save = orderTableRepository.save(orderTable);
        long requestTableId = save.getId();
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
        long requestTableId = 주문_테이블_등록됨(주문_테이블);
        OrderTableRequest request = 테이블_요청_만들기(-1);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request))
                .withMessageContaining("방문 손님 수가 0명 미만일 수 없습니다.");
    }

    @DisplayName("빈 테이블인 경우 방문 손님 수를 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_empty_table() {
        //given
        OrderTable orderTable = 테이블_만들기(3, true);
        long requestTableId = 주문_테이블_등록됨(orderTable);
        OrderTableRequest request = 테이블_요청_만들기(5);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request));
    }

    private long 주문_테이블_등록됨(OrderTable orderTable) {
        OrderTable save = orderTableRepository.save(orderTable);
        return save.getId();
    }

}
