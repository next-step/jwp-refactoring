package kitchenpos.utils.generator;

import static kitchenpos.ui.TableGroupRestControllerTest.TABLE_GROUP_API_BASE_URL;
import static kitchenpos.utils.MockMvcUtil.getRequestBuilder;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.CreateTableGroupRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class TableGroupFixtureGenerator {

    public static CreateTableGroupRequest 테이블_그룹_생성(final OrderTable... savedOrderTables) {
        List<Long> orderTableIds = Arrays.stream(savedOrderTables)
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        return new CreateTableGroupRequest(orderTableIds);
    }

    public static CreateTableGroupRequest 테이블_그룹_생성(final OrderTableResponse... savedOrderTables) {
        List<Long> orderTableIds = Arrays.stream(savedOrderTables)
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());
        return new CreateTableGroupRequest(orderTableIds);
    }

    public static MockHttpServletRequestBuilder 테이블_그룹_생성_요청(final OrderTableResponse... createOrderTableRequests)
        throws Exception {
        return postRequestBuilder(TABLE_GROUP_API_BASE_URL, 테이블_그룹_생성(createOrderTableRequests));
    }

    public static MockHttpServletRequestBuilder 테이블_그룹_조회_요청(final Long path) {
        final String GET_TABLE_GROUP_API_TEMPLATE = TABLE_GROUP_API_BASE_URL.concat("/{id}");
        return getRequestBuilder(GET_TABLE_GROUP_API_TEMPLATE, path);
    }
}