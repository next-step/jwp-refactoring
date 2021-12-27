package kitchenpos.table.fixtures;

import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeGuestNumberRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableSaveRequest;
import kitchenpos.table.domain.OrderTable;

/**
 * packageName : kitchenpos.fixtures
 * fileName : TableFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class OrderTableFixtures {
    public static OrderTableSaveRequest 주문불가_다섯명테이블요청() {
        return OrderTableSaveRequest.of(5, true);
    }

    public static OrderTableSaveRequest 주문불가_두명테이블요청() {
        return OrderTableSaveRequest.of(2, true);
    }

    public static OrderTableSaveRequest 주문가능_다섯명테이블요청() {
        return OrderTableSaveRequest.of(5, false);
    }

    public static OrderTableSaveRequest 주문가능_두명테이블요청() {
        return OrderTableSaveRequest.of(2, false);
    }

    public static OrderTable 주문가능_다섯명테이블() {
        return 주문가능_다섯명테이블요청().toEntity();
    }

    public static OrderTable 주문가능_두명테이블() {
        return 주문가능_두명테이블요청().toEntity();
    }

    public static OrderTable 주문불가_다섯명테이블() {
        return 주문불가_다섯명테이블요청().toEntity();
    }

    public static OrderTable 주문불가_두명테이블() {
        return 주문불가_두명테이블요청().toEntity();
    }

    public static OrderTable 그룹화된_테이블() {
        OrderTable orderTable = new OrderTable(3, false);
        orderTable.groupBy(1L);
        return orderTable;
    }
    public static OrderTable 그룹화되지_않은_테이블() {
        return new OrderTable(3, false);
    }
    public static ChangeEmptyRequest 주문불가로_변경요청() {
        return ChangeEmptyRequest.of(true);
    }

    public static ChangeEmptyRequest 주문가능으로_변경요청() {
        return ChangeEmptyRequest.of(false);
    }

    public static ChangeGuestNumberRequest 다섯명으로_변경요청() {
        return ChangeGuestNumberRequest.of(5);
    }

    public static ChangeGuestNumberRequest 두명으로_변경요청() {
        return ChangeGuestNumberRequest.of(2);
    }

    public static ChangeGuestNumberRequest 사용자수_변경요청(Integer numberOfGuests) {
        return ChangeGuestNumberRequest.of(numberOfGuests);
    }

    public static OrderTableRequest 테이블_그룹요청() {
        return OrderTableRequest.of(1L);
    }

    public static OrderTableRequest 테이블_그룹요청(Long tableId) {
        return OrderTableRequest.of(tableId);
    }
}

