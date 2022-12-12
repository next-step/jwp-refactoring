package kitchenpos.ui;

import static kitchenpos.ui.MenuGroupRestControllerTest.메뉴_그룹_생성_요청;
import static kitchenpos.ui.MenuRestControllerTest.메뉴_생성_요청;
import static kitchenpos.ui.OrderRestControllerTest.주문_생성_요청;
import static kitchenpos.ui.ProductRestControllerTest.상품_생성_요청;
import static kitchenpos.ui.TableRestControllerTest.좌석_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

class TableGroupRestControllerTest extends BaseTest {
    private final MenuProduct 메뉴_항목 = new MenuProduct(1L, 1);
    private final MenuGroup 메뉴_그룹 = new MenuGroup(1L, "한마리메뉴");
    private final List<MenuProduct> 메뉴_항목들 = Arrays.asList(메뉴_항목);
    private final Menu 메뉴 = new Menu("후라이드치킨", BigDecimal.valueOf(16000), 메뉴_그룹.getId(), 메뉴_항목들);
    private final Product 상품 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
    private final OrderTable 좌석_1 = new OrderTable(null, 1, true);
    private final OrderTable 좌석_2 = new OrderTable(null, 1, true);
    private final List<OrderLineItem> 주문_항목들 = Arrays.asList(
            new OrderLineItem(1L, 1L, 1L, 1));
    private Order 주문_1;
    private Order 주문_2;
    private final List<OrderTable> 좌석_목록 = Arrays.asList(좌석_1, 좌석_2);
    private final TableGroup 좌석_그룹 = new TableGroup(LocalDateTime.now(), 좌석_목록);

    @Test
    void 생성() {
        메뉴_그룹_생성_요청(메뉴_그룹);
        상품_생성_요청(상품);
        메뉴_생성_요청(메뉴);
        Long orderTableId1 = 좌석_생성_요청(좌석_1).getBody().getId();
        Long orderTableId2 = 좌석_생성_요청(좌석_2).getBody().getId();
        좌석_1.setId(orderTableId1);
        좌석_2.setId(orderTableId2);
        주문_1 = new Order(orderTableId1, null, LocalDateTime.now(), 주문_항목들);
        주문_2 = new Order(orderTableId2, null, LocalDateTime.now(), 주문_항목들);
        주문_생성_요청(주문_1);
        주문_생성_요청(주문_2);

        ResponseEntity<TableGroup> response = 좌석_그룹_생성_요청(좌석_그룹);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 제거() {
        메뉴_그룹_생성_요청(메뉴_그룹);
        상품_생성_요청(상품);
        메뉴_생성_요청(메뉴);
        Long orderTableId1 = 좌석_생성_요청(좌석_1).getBody().getId();
        Long orderTableId2 = 좌석_생성_요청(좌석_2).getBody().getId();
        좌석_1.setId(orderTableId1);
        좌석_2.setId(orderTableId2);
        주문_1 = new Order(orderTableId1, null, LocalDateTime.now(), 주문_항목들);
        주문_2 = new Order(orderTableId2, null, LocalDateTime.now(), 주문_항목들);
        주문_생성_요청(주문_1);
        주문_생성_요청(주문_2);
        Long tableGroupId = 좌석_그룹_생성_요청(좌석_그룹).getBody().getId();

        ResponseEntity<Void> response = 좌석_그룹_제거_요청(tableGroupId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<TableGroup> 좌석_그룹_생성_요청(TableGroup tableGroup) {
        return testRestTemplate.postForEntity(basePath + "/api/table-groups", tableGroup, TableGroup.class);
    }

    private ResponseEntity<Void> 좌석_그룹_제거_요청(Long tableGroupId) {
        return testRestTemplate.exchange(
                basePath + "/api/table-groups/" + tableGroupId,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Void>() {});
    }
}
