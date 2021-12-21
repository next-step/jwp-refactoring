package kitchenpos.application.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;

public class TableGroupTestFixtures {

    public static void 테이블그룹_저장_결과_모킹(TableGroupDao tableGroupDao, TableGroup tableGroup) {
        given(tableGroupDao.save(any()))
            .willReturn(tableGroup);
    }
}
