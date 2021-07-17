package kitchenpos.table.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.table.domain.entity.TableGroup;
import kitchenpos.table.domain.value.NumberOfGuests;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.service.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest {

    @Autowired
    TableService tableService;

    OrderTable 주문테이블_테이블1;
    OrderTable 주문테이블_테이블2;
    OrderTable 주문테이블_상태변경테이블;
    OrderTable 주문테이블_인원변경테이블;

    long 존재하지않는아이디;
    OrderTableRequest 주문테이블_테이블1_리퀘스트;
    OrderTableRequest 주문테이블_상태변경테이블_리퀘스트;
    OrderTableRequest 주문테이블_인원변경테이블_리퀘스트;

    @BeforeEach
    void setUp() {
        존재하지않는아이디 = 999L;

        TableGroup 테이블그룹 = new TableGroup();

        주문테이블_테이블1 = new OrderTable(1L, NumberOfGuests.of(3), false);

        주문테이블_테이블1_리퀘스트 = new OrderTableRequest();

        주문테이블_테이블2 = new OrderTable(1L, NumberOfGuests.of(3), true);

        주문테이블_상태변경테이블 = new OrderTable();

        주문테이블_인원변경테이블 = new OrderTable(1L, NumberOfGuests.of(4), false);

        주문테이블_인원변경테이블_리퀘스트 = new OrderTableRequest(1L, 4, false);

        주문테이블_상태변경테이블_리퀘스트 = new OrderTableRequest(1L, 3, false);
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void create_re() {
        //when
        OrderTableResponse createdOrderTable = tableService.create(주문테이블_테이블1_리퀘스트);

        //then
        assertThat(createdOrderTable).isNotNull();
    }

    @Test
    @DisplayName("전체 테이블을 조회한다.")
    void list_re() {
        //when
        List<OrderTableResponse> orderTables = tableService.list();

        //then
        assertThat(orderTables.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmpty_re() {
        //when
        OrderTableResponse changedOrderTable = tableService
            .changeEmpty(주문테이블_테이블1.getId(), 주문테이블_상태변경테이블_리퀘스트);

        //then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("존재하지않는 테이블의 상태변경은 실패한다.")
    void changeEmpty_with_exception_when_not_exist_orderTableId_re() {
        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(존재하지않는아이디, 주문테이블_상태변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("그룹테이블로 지정되어있을경우 상태변경은 실패한다.")
    void changeEmpty_with_exception_re() {
        주문테이블_테이블1 = new OrderTable(98L, NumberOfGuests.of(3), false);
        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블_테이블1.getId(), 주문테이블_상태변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조리 또는 식사중일 경우 상태변경은 실패한다.")
    void changeEmpty_when_orderStatus_in_cooking_or_meal_re() {
        //given
        주문테이블_테이블1 = new OrderTable(21L, NumberOfGuests.of(3), false);
        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블_테이블1.getId(), 주문테이블_상태변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 손님 인원을 변경한다.")
    void changeNumberOfGuests_re() {
        //when
        주문테이블_테이블1 = new OrderTable(7L, NumberOfGuests.of(3), false);
        OrderTableResponse changedOrderTable = tableService
            .changeNumberOfGuests(주문테이블_테이블1.getId(), 주문테이블_인원변경테이블_리퀘스트);

        //then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    @DisplayName("변경인원이 0명 미만일 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_person_smaller_than_zero_re() {
        //given

        //when && then
        assertThatThrownBy(() -> {
            주문테이블_테이블1 = new OrderTable(7L, NumberOfGuests.of(-3), false);
            tableService.changeNumberOfGuests(주문테이블_테이블1.getId(), 주문테이블_인원변경테이블_리퀘스트);
        })
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("없는테이블의 인원을 변경할 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_not_exist_orderTableId_re() {
        //when && then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(존재하지않는아이디, 주문테이블_인원변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 테이블의 인원을 변경할 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_orderTable_isEmpty_re() {
        //when && then
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(주문테이블_테이블2.getId(), 주문테이블_인원변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }
}