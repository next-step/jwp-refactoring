package kitchenpos.utils.generator;

import static kitchenpos.ui.TableRestControllerTest.TABLE_API_BASE_URL;
import static kitchenpos.ui.TableRestControllerTest.UPDATE_NUMBER_OF_GUEST_API_URL_TEMPLATE;
import static kitchenpos.ui.TableRestControllerTest.UPDATE_TABLE_EMPTY_URL_TEMPLATE;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;
import static kitchenpos.utils.MockMvcUtil.putRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.table.OrderTable;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class OrderTableFixtureGenerator {

    private static int NUMBER_OF_GUESTS = 0;
    private static boolean EMPTY = false;
    private static int COUNTER = 0;

    public static OrderTable 비어있는_주문_테이블_생성() {
        COUNTER++;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(NUMBER_OF_GUESTS + COUNTER);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable 비어있지_않은_주문_테이블_생성() {
        OrderTable orderTable = 비어있는_주문_테이블_생성();
        orderTable.setEmpty(EMPTY);
        return orderTable;
    }

    public static List<OrderTable> 비어있지_않은_주문_테이블_목록_생성(int count) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            orderTables.add(비어있지_않은_주문_테이블_생성());
        }
        return orderTables;
    }

    public static MockHttpServletRequestBuilder 비어있지_않은_주문_테이블_생성_요청() throws Exception {
        return postRequestBuilder(TABLE_API_BASE_URL, 비어있지_않은_주문_테이블_생성());
    }

    public static MockHttpServletRequestBuilder 비어있는_주문_테이블_생성_요청() throws Exception {
        return postRequestBuilder(TABLE_API_BASE_URL, 비어있는_주문_테이블_생성());
    }

    public static MockHttpServletRequestBuilder 테이블_사용_가능_여부_수정_요청_생성(
        final OrderTable updateOrderTableEmptyRequest,
        final Long savedOrderId
    ) throws Exception {
        return putRequestBuilder(UPDATE_TABLE_EMPTY_URL_TEMPLATE, updateOrderTableEmptyRequest, savedOrderId);
    }

    public static MockHttpServletRequestBuilder 테이블_객수_수정_요청_생성(
        final OrderTable updateNumberOfGuestsRequest,
        final Long savedOrderId
    ) throws Exception {
        return putRequestBuilder(UPDATE_NUMBER_OF_GUEST_API_URL_TEMPLATE, updateNumberOfGuestsRequest, savedOrderId);
    }
}
