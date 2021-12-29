package kitchenpos.tablegroup.fixtures;

import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupSaveRequest;
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
    public static TableGroupSaveRequest 그룹테이블_그룹요청() {
        return TableGroupSaveRequest.of(Lists.newArrayList(테이블_그룹요청(), 테이블_그룹요청()));
    }

    public static TableGroupSaveRequest 그룹테이블_그룹요청(List<OrderTableRequest> orderTableRequests) {
        return  TableGroupSaveRequest.of(orderTableRequests);
    }

    public static TableGroupSaveRequest 그룹테이블_그룹요청_예외_테이블한개() {
        return TableGroupSaveRequest.of(Lists.newArrayList(테이블_그룹요청()));
    }
}
