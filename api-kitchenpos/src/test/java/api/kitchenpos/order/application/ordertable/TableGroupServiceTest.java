package api.kitchenpos.order.application.ordertable;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import api.kitchenpos.order.dto.ordertable.OrderTableRequest;
import api.kitchenpos.order.dto.ordertable.TableGroupRequest;
import api.kitchenpos.order.dto.ordertable.TableGroupResponse;
import domain.kitchenpos.menu.menu.Menu;
import domain.kitchenpos.menu.menu.MenuGroup;
import domain.kitchenpos.menu.menu.MenuProduct;
import domain.kitchenpos.menu.product.Product;
import domain.kitchenpos.order.order.Order;
import domain.kitchenpos.order.order.OrderLineItem;
import domain.kitchenpos.order.ordertable.OrderTable;
import domain.kitchenpos.order.ordertable.OrderTableRepository;
import domain.kitchenpos.order.ordertable.TableGroup;
import domain.kitchenpos.order.ordertable.TableGroupRepository;

@DisplayName("애플리케이션 테스트 보호 - 단체 지정 서비스")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuGroup 치킨세트;
    private Menu 후라이드한마리_양념치킨한마리;
    private MenuProduct 후라이드치킨한마리;
    private MenuProduct 양념치킨한마리;
    private OrderLineItem 주문_항목;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderTable 주문테이블_비어있지_않음;
    private OrderTable 주문테이블_단체지정됨1;
    private OrderTable 주문테이블_단체지정됨2;
    private List<OrderLineItem> 주문_항목_목록;
    private TableGroup 단체;
    private TableGroupRequest 단체_지정_요청;
    private List<OrderTable> 빈_주문테이블_목록;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @BeforeEach
    public void setup() {
        주문테이블1 = new OrderTable(0, true);
        주문테이블2 = new OrderTable(0, true);
        주문테이블_비어있지_않음 = new OrderTable(2, false);
        주문테이블_단체지정됨1 = new OrderTable(0, true);
        주문테이블_단체지정됨2 = new OrderTable(0, true);
        단체 = TableGroup.createTableGroup(Arrays.asList(주문테이블_단체지정됨1, 주문테이블_단체지정됨2));

        후라이드치킨 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(16000));

        치킨세트 = new MenuGroup("후라이드앙념치킨");

        후라이드한마리_양념치킨한마리 = new Menu("후라이드+양념", BigDecimal.valueOf(32000), 치킨세트);
        후라이드치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 후라이드치킨, 1);
        양념치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 양념치킨, 1);
        후라이드한마리_양념치킨한마리.addMenuProducts(Arrays.asList(후라이드치킨한마리, 양념치킨한마리));
        주문_항목 = new OrderLineItem(후라이드한마리_양념치킨한마리, 1L);
        주문_항목_목록 = new ArrayList<>();
        주문_항목_목록.add(주문_항목);

        List<OrderTableRequest> orderTableRequests = new ArrayList<>();
        orderTableRequests.add(new OrderTableRequest(1L));
        orderTableRequests.add(new OrderTableRequest(2L));
        단체_지정_요청 = new TableGroupRequest(orderTableRequests);

        빈_주문테이블_목록 = Arrays.asList(주문테이블1, 주문테이블2);
    }

    @DisplayName("단체 지정 생성")
    @Test
    void create() {

        given(orderTableRepository.findAllByIdIn(단체_지정_요청.getOrderTableIds())).willReturn(빈_주문테이블_목록);
        given(tableGroupRepository.save(단체)).willReturn(단체);
        TableGroupResponse tableGroupResponse = tableGroupService.create(단체_지정_요청);

        assertThat(tableGroupResponse).isNotNull();
        assertThat(tableGroupResponse.getOrderTableResponses()).isNotEmpty();

    }

    @DisplayName("단체 지정 생성 예외: 주문 테이블 목록이 비어있음")
    @Test
    void createThrowExceptionWhenOrderTablesIsEmpty() {
        단체_지정_요청.setOrderTables(null);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체_지정_요청));
    }

    @DisplayName("단체 지정 생성 예외: 주문 테이블 목록 갯수가 2보다 적음")
    @Test
    void createThrowExceptionWhenOrderTablesSizeLessThen2() {
        단체_지정_요청.setOrderTables(Collections.singletonList(new OrderTableRequest(1L)));
        given(orderTableRepository.findAllByIdIn(단체_지정_요청.getOrderTableIds())).willReturn(빈_주문테이블_목록);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체_지정_요청));
    }

    @DisplayName("단체 지정 생성 예외: 주문 테이블이 저장된 데이터가 아님")
    @Test
    void createThrowExceptionWhenOrderTablesNotExistsInStorage() {
        given(orderTableRepository.findAllByIdIn(단체_지정_요청.getOrderTableIds()))
            .willReturn(Collections.singletonList(주문테이블1));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체_지정_요청));
    }

    @DisplayName("단체 지정 생성 예외: 주문 테이블 중에 빈 테이블이 아닌 것이 있음")
    @Test
    void createThrowExceptionWhenHasNotEmptyOrderTable() {
        given(orderTableRepository.findAllByIdIn(단체_지정_요청.getOrderTableIds()))
            .willReturn(Collections.singletonList(주문테이블_비어있지_않음));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체_지정_요청));
    }

    @DisplayName("단체 지정 생성 예외: 주문 테이블 중에 이미 단체지정된 것이 있음")
    @Test
    void createThrowExceptionWhenHasAlreadyTableGroupOrderTable() {
        given(orderTableRepository.findAllByIdIn(단체_지정_요청.getOrderTableIds()))
            .willReturn(Collections.singletonList(주문테이블_단체지정됨1));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체_지정_요청));
    }

    @DisplayName("주문 테이블의 단체 지정 삭제")
    @Test
    void ungroup() {
        final Long tableGroupId = 1L;
        given(tableGroupRepository.findById(tableGroupId)).willReturn(Optional.of(단체));

        tableGroupService.ungroup(tableGroupId);

        assertThat(주문테이블1.getTableGroup()).isNull();
        assertThat(주문테이블2.getTableGroup()).isNull();

    }

    @DisplayName("주문 테이블의 단체 지정 삭제 예외: 주문 상태가 조리 또는 식사임")
    @Test
    void ungroupThrowExceptionOrderStatusIsCookingOrMeal() {
        final Long tableGroupId = 1L;
        단체 = TableGroup.createTableGroup(빈_주문테이블_목록);
        final Order 주문 = Order.createToCook(주문테이블1, 주문_항목_목록);

        given(tableGroupRepository.findById(tableGroupId)).willReturn(Optional.of(단체));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(tableGroupId));
    }

}
