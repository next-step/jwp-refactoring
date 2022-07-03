package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.table.TableGenerator.주문_테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TableServiceTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @DisplayName("주문 테이블 생성 시 정상 생성되어야 한다")
    @Test
    void createOrderTableTest() {
        // given
        int numberOfGuests = 10;
        OrderTableCreateRequest 주문_테이블_생성_요청 = 주문_테이블_생성_요청(numberOfGuests);

        // when
        OrderTableResponse 생성_된_주문_테이블 = tableService.create(주문_테이블_생성_요청);

        // then
        주문_테이블_저장됨(생성_된_주문_테이블, 주문_테이블_생성_요청);
    }

    @DisplayName("주문 테이블 목록 조회 시 정상 조회되어야 한다")
    @Test
    void findAllOrderTableTest() {
        // given
        List<Long> 포함되어야_할_아이디들 = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            포함되어야_할_아이디들.add(
                    tableService.create(주문_테이블_생성_요청(10)).getId()
            );
        }

        // when
        List<OrderTableResponse> 주문_테이블_목록_조회_결과 = tableService.list();

        // then
        주문_테이블_목록_조회됨(주문_테이블_목록_조회_결과, 포함되어야_할_아이디들);
    }

    @DisplayName("저장되지 않은 주문 테이블의 빈 자리 여부를 변경하면 예외가 발생해야 한다")
    @Test
    void changeEmptyByBeforeSavedOrderTableTest() {
        주문_테이블_인원_변경_실패됨(() -> tableService.changeEmpty(-1L, false));
    }

    @DisplayName("요리중 또는 식사중인 주문 테이블의 빈 테이블 여부를 변경하면 예외가 발생해야 한다")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL" })
    void changeEmptyByCockingOrMealStatusOrderTableTest(OrderStatus orderStatus) {
        // given
        when(orderService.getOrderStatusByOrderTableId(any())).thenReturn(orderStatus);
        OrderTableResponse 생성_된_주문_테이블 = tableService.create(주문_테이블_생성_요청(10));

        // then
        주문_테이블_인원_변경_실패됨(() -> tableService.changeEmpty(생성_된_주문_테이블.getId(), false));
    }

    @DisplayName("정상 상태의 주문 테이블의 빈 자리 여부를 변경하면 정상 변경되어야 한다")
    @Test
    void changeEmptyTest() {
        // given
        OrderTableResponse 생성_된_주문_테이블 = tableService.create(주문_테이블_생성_요청(10));

        // when
        OrderTableResponse 빈_자리_상태_변경_된_주문_테이블 = tableService.changeEmpty(생성_된_주문_테이블.getId(), false);

        // then
        빈_자리_여부_상태_변경_성공됨(빈_자리_상태_변경_된_주문_테이블, false);
    }

    @DisplayName("저장되지 않은 주문 테이블의 인원을 변경하면 예외가 발생해야 한다")
    @Test
    void changeNumberOfGuestsByBeforeSavedOrderTableTest() {
        주문_테이블_인원_변경_실패됨(() -> tableService.changeNumberOfGuests(-1L, 10));
    }

    @DisplayName("테이블 인원 변경 시 테이블의 인원이 0명 미만일 경우 예외가 발생해야 한다")
    @Test
    void changeNumberOfGuestByMinusGuests() {
        // given
        OrderTableResponse 생성_된_주문_테이블 = tableService.create(주문_테이블_생성_요청(10));

        // then
        주문_테이블_인원_변경_실패됨(() -> tableService.changeNumberOfGuests(생성_된_주문_테이블.getId(), -1));
    }

    @DisplayName("변경 할 주문 테이블이 빈 자리인 경우 인원을 변경하면 예외가 발생해야 한다")
    @Test
    void changeNumberOfGuestsByEmptyOrderTableTest() {
        // given
        OrderTableResponse 생성_된_주문_테이블 = tableService.create(주문_테이블_생성_요청(10));

        // then
        주문_테이블_인원_변경_실패됨(() -> tableService.changeNumberOfGuests(생성_된_주문_테이블.getId(), 5));
    }

    @DisplayName("정상 상태의 주문 테이블의 인원을 변경하면 정상 변경되어야 한다")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        when(orderService.getOrderStatusByOrderTableId(any())).thenReturn(OrderStatus.COMPLETION);
        OrderTableResponse 생성_된_주문_테이블 = tableService.create(주문_테이블_생성_요청(10));
        tableService.changeEmpty(생성_된_주문_테이블.getId(), false);

        // when
        tableService.changeNumberOfGuests(생성_된_주문_테이블.getId(), 5);

        // then
        OrderTable orderTable = tableService.getOrderTable(생성_된_주문_테이블.getId());
        주문_테이블_인원_변경_성공됨(orderTable, 5);
    }

    void 주문_테이블_저장됨(OrderTableResponse orderTable, OrderTableCreateRequest request) {
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        assertThat(orderTable.isEmpty()).isTrue();
    }

    void 주문_테이블_목록_조회됨(List<OrderTableResponse> orderTables, List<Long> containIds) {
        assertThat(orderTables.size()).isGreaterThanOrEqualTo(containIds.size());
        assertThat(orderTables.stream().mapToLong(OrderTableResponse::getId)).containsAll(containIds);
    }

    void 빈_자리_여부_상태_변경_성공됨(OrderTableResponse orderTable, boolean empty) {
        assertThat(orderTable.isEmpty()).isEqualTo(empty);
    }

    void 주문_테이블_인원_변경_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 주문_테이블_인원_변경_성공됨(OrderTable orderTable, int expected) {
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(expected));
    }
}
