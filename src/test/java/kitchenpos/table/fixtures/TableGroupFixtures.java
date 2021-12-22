package kitchenpos.table.fixtures;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupSaveRequest;
import org.assertj.core.util.Lists;

import java.util.List;

import static kitchenpos.table.fixtures.OrderTableFixtures.*;

/**
 * packageName : kitchenpos.fixtures
 * fileName : TableGroupFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class TableGroupFixtures {
    public static TableGroup 주문불가_5인_2인_그룹테이블() {
        return new TableGroup(Lists.newArrayList(주문불가_다섯명테이블(), 주문불가_두명테이블()));
    }

    public static TableGroupSaveRequest 그룹테이블_그룹요청() {
        return new TableGroupSaveRequest(Lists.newArrayList(테이블_그룹요청(), 테이블_그룹요청()));
    }

    public static TableGroupSaveRequest 그룹테이블_그룹요청(List<OrderTableRequest> orderTableRequests) {
        return new TableGroupSaveRequest(orderTableRequests);
    }

    public static TableGroupSaveRequest 그룹테이블_그룹요청_예외_테이블한개() {
        return new TableGroupSaveRequest(Lists.newArrayList(테이블_그룹요청()));
    }
}
