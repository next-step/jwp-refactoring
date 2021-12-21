package kitchenpos.fixtures;

import kitchenpos.dto.OrderTableSaveRequest;

/**
 * packageName : kitchenpos.fixtures
 * fileName : TableFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class OrderTableFixtures {
    public static OrderTableSaveRequest 사용불가_다섯명테이블() {
        return OrderTableSaveRequest.of(5, true);
    }

    public static OrderTableSaveRequest 사용가능_다섯명테이블() {
        return OrderTableSaveRequest.of(5, false);
    }
}
