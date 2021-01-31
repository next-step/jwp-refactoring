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
import api.kitchenpos.order.dto.ordertable.OrderTableResponse;
import domain.kitchenpos.menu.menu.Menu;
import domain.kitchenpos.menu.menu.MenuGroup;
import domain.kitchenpos.menu.menu.MenuProduct;
import domain.kitchenpos.menu.product.Product;
import domain.kitchenpos.order.order.Order;
import domain.kitchenpos.order.order.OrderLineItem;
import domain.kitchenpos.order.ordertable.OrderTable;
import domain.kitchenpos.order.ordertable.OrderTableRepository;
import domain.kitchenpos.order.ordertable.TableGroup;

@DisplayName("애플리케이션 테스트 보호 - 주문테이블 서비스")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuGroup 치킨세트;
    private Menu 후라이드한마리_양념치킨한마리;
    private MenuProduct 후라이드치킨한마리;
    private MenuProduct 양념치킨한마리;

    private OrderLineItem 주문_항목;
    private List<OrderLineItem> 주문_항목_목록;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderTable 주문테이블_비어있지_않음;
    private OrderTableRequest 주문테이블_요청;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    public void setup() {
        후라이드치킨 = new Product(1L, "후라이드치킨", new BigDecimal(16000));
        양념치킨 = new Product(2L, "양념치킨", new BigDecimal(16000));

        치킨세트 = new MenuGroup("후라이드앙념치킨");

        후라이드한마리_양념치킨한마리 = new Menu("후라이드+양념", BigDecimal.valueOf(32000), 치킨세트);

        후라이드치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 후라이드치킨, 1L);
        양념치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 양념치킨, 1L);

        후라이드한마리_양념치킨한마리.addMenuProducts(Arrays.asList(후라이드치킨한마리, 양념치킨한마리));

        주문테이블_요청 = new OrderTableRequest(0, true);
        주문테이블1 = 주문테이블_요청.toOrderTable();
        주문테이블2 = 주문테이블_요청.toOrderTable();

        주문테이블_비어있지_않음 = 주문테이블_요청.toOrderTable();
        주문테이블_비어있지_않음.changeEmpty(false);
        주문테이블_비어있지_않음.changeNumberOfGuests(2);
        
        주문_항목 = new OrderLineItem(후라이드한마리_양념치킨한마리, 1L);
        주문_항목_목록 = new ArrayList<>();
        주문_항목_목록.add(주문_항목);

    }

    @DisplayName("주문테이블 생성")
    @Test
    void create() {
        given(orderTableRepository.save(주문테이블1)).willReturn(주문테이블1);

        OrderTableResponse orderTableResponse = tableService.create(주문테이블_요청);

        assertThat(orderTableResponse).isNotNull();
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(주문테이블_요청.getNumberOfGuests());

    }

    @DisplayName("주문테이블 목록 조회")
    @Test
    void list() {
        given(orderTableRepository.findAll()).willReturn(Collections.singletonList(주문테이블1));

        List<OrderTableResponse> tables = tableService.findAll();

        assertThat(tables).isNotEmpty();
        assertThat(tables.get(0).getNumberOfGuests()).isEqualTo(주문테이블1.getNumberOfGuests());
    }

    @DisplayName("주문테이블의 빈 테이블 여부를 변경한다.")
    @Test
    void changeEmpty() {
        final Long orderTableId = 1L;
        주문테이블_요청.setEmpty(false);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(주문테이블1));
        OrderTableResponse orderTableResponse = tableService.changeEmpty(orderTableId, 주문테이블_요청);

        assertThat(orderTableResponse.isEmpty()).isEqualTo(주문테이블_요청.isEmpty());
    }

    @DisplayName("주문테이블의 빈 테이블 여부를 변경 예외: 주문테이블이 없음")
    @Test
    void changeEmptyThrowExceptionWhenNoOrderTable() {
        final Long orderTableId = 1L;
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, 주문테이블_요청));
    }

    @DisplayName("주문테이블의 빈 테이블 여부를 변경 예외: 테이블그룹이 있음")
    @Test
    void changeEmptyThrowExceptionWhenTableGroupIdExists() {
        final Long orderTableId = 1L;

        final TableGroup 단체 = TableGroup.createTableGroup(Arrays.asList(주문테이블1, 주문테이블2));

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(주문테이블1));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, 주문테이블_요청));
    }

    @DisplayName("주문테이블의 빈 테이블 여부를 변경 예외: 주문테이블의 주문 상태가 조리 또는 식사임")
    @Test
    void changeEmptyThrowExceptionWhenTableOrderStatusCookingOrMeal() {
        주문테이블1.changeEmpty(false);
        Order.createToCook(주문테이블1, 주문_항목_목록);

        given(orderTableRepository.findById(주문테이블1.getId())).willReturn(Optional.of(주문테이블1));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(주문테이블1.getId(), 주문테이블_요청));
    }

    @DisplayName("주문테이블의 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final Long orderTableId = 1L;
        final int expected = 10;
        주문테이블_요청.setNumberOfGuests(expected);
        주문테이블1.changeEmpty(false);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(주문테이블1));

        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(orderTableId, 주문테이블_요청);

        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(expected);
    }

    @DisplayName("주문테이블의 방문한 손님 수를 변경 예외: 방문한 손님 수가 0보다 작음")
    @Test
    void changeNumberOfGuestsThrowExceptionWhenNumberOfGuestsLessThanZero() {
        주문테이블_요청.setNumberOfGuests(-1);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블1.getId(), 주문테이블_요청));
    }

    @DisplayName("주문테이블의 방문한 손님 수를 변경 예외: 주문 테이블 정보가 없음")
    @Test
    void changeNumberOfGuestsThrowExceptionWhenNoOrderTable() {
        주문테이블_요청.setNumberOfGuests(10);
        given(orderTableRepository.findById(주문테이블1.getId())).willReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블1.getId(), 주문테이블_요청));
    }

    @DisplayName("주문테이블의 방문한 손님 수를 변경 예외: 빈 테이블임")
    @Test
    void changeNumberOfGuestsThrowExceptionWhenOrderTableIsEmpty() {
        주문테이블_요청.setNumberOfGuests(10);
        주문테이블_요청.setEmpty(true);
        given(orderTableRepository.findById(주문테이블1.getId())).willReturn(Optional.of(주문테이블1));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블1.getId(), 주문테이블_요청));
    }

}
