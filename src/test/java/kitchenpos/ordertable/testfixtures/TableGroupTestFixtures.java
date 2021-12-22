package kitchenpos.ordertable.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupDao;

public class TableGroupTestFixtures {

    public static void 테이블그룹_저장_결과_모킹(TableGroupDao tableGroupDao, TableGroup tableGroup) {
        given(tableGroupDao.save(any()))
            .willReturn(tableGroup);
    }
}
