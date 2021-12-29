package api.tablegroup;

import api.table.dto.OrderTableRequest;
import api.tablegroup.dto.TableGroupSaveRequest;

import java.util.List;

import static java.util.Arrays.asList;
import static api.table.OrderTableFixtures.테이블_그룹요청;

/**
 * packageName : kitchenpos.fixtures
 * fileName : TableGroupFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class TableGroupFixtures {
    public static TableGroupSaveRequest 그룹테이블_그룹요청() {
        return TableGroupSaveRequest.of(asList(테이블_그룹요청(), 테이블_그룹요청()));
    }

    public static TableGroupSaveRequest 그룹테이블_그룹요청(List<OrderTableRequest> orderTableRequests) {
        return  TableGroupSaveRequest.of(orderTableRequests);
    }

    public static TableGroupSaveRequest 그룹테이블_그룹요청_예외_테이블한개() {
        return TableGroupSaveRequest.of(asList(테이블_그룹요청()));
    }
}
