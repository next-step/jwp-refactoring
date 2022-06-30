package kitchenpos.utils.generator;

import static kitchenpos.ui.TableGroupRestControllerTest.TABLE_GROUP_API_BASE_URL;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;

import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class TableGroupFixtureGenerator {

    public static TableGroup generateTableGroup(final OrderTable... savedOrderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(savedOrderTables));
        return tableGroup;
    }

    public static MockHttpServletRequestBuilder 테이블_그룹_생성_요청(final OrderTable... savedOrderTables) throws Exception {
        return postRequestBuilder(TABLE_GROUP_API_BASE_URL, generateTableGroup(savedOrderTables));
    }
}
