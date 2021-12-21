package kitchenpos.fixtures;

import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;

import static kitchenpos.fixtures.OrderTableFixtures.주문불가_다섯명테이블;
import static kitchenpos.fixtures.OrderTableFixtures.주문불가_두명테이블;

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
}
