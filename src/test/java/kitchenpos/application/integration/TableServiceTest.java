package kitchenpos.application.integration;

import kitchenpos.application.OrderService;
import kitchenpos.application.TableService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.*;
import kitchenpos.repository.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 서비스 통합 테스트")
@Transactional
@SpringBootTest
public class TableServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("주문테이블 등록")
    @Test
    public void 등록_확인() throws Exception {
        //given
        TableRequest tableRequest = new TableRequest(5, false);

        //when
        TableResponse tableResponse = tableService.create(tableRequest);

        //then
        assertThat(tableResponse.getId()).isNotNull();
    }

    @DisplayName("주문테이블 목록 조회")
    @Test
    public void 목록_조회_확인() throws Exception {
        //given
        TableRequest tableRequest = new TableRequest(5, false);
        tableService.create(tableRequest);
        tableService.create(tableRequest);
        tableService.create(tableRequest);

        //when
        List<TableResponse> tableResponses = tableService.list();

        //then
        assertThat(tableResponses.size()).isEqualTo(3);
    }

    @DisplayName("주문테이블 빈테이블 여부 변경")
    @Test
    public void 빈테이블여부_변경_확인() throws Exception {
        //given
        TableRequest tableRequest = new TableRequest(5, false);
        TableResponse tableResponse = tableService.create(tableRequest);

        //when
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);
        TableResponse changeEmptyTableResponse = tableService.changeEmpty(tableResponse.getId(), tableEmptyRequest);

        //then
        assertThat(changeEmptyTableResponse.isEmpty()).isTrue();
    }

    @DisplayName("주문테이블 빈테이블 여부 변경 예외 - 주문테이블이 존재하지않는 경우")
    @Test
    public void 주문테이블이존재하지않는경우_빈테이블여부_변경_예외() throws Exception {
        //given
        TableRequest tableRequest = new TableRequest(5, false);
        tableService.create(tableRequest);

        //when
        //then
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, tableEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 빈테이블 여부 변경 예외 - 주문상태가 조리인 경우")
    @Test
    public void 주문상태가조리인경우_빈테이블여부_변경_예외() throws Exception {
        TableRequest tableRequest = new TableRequest(5, false);
        TableResponse tableResponse = tableService.create(tableRequest);
        Menu menu = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(1000), null));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
        OrderRequest orderRequest = new OrderRequest(tableResponse.getId(), Arrays.asList(orderLineItemRequest));
        OrderResponse orderResponse = orderService.create(orderRequest);
        orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(OrderStatus.COOKING));

        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);
        assertThatThrownBy(() -> tableService.changeEmpty(tableResponse.getId(), tableEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 빈테이블 여부 변경 예외 - 주문상태가 식사인 경우")
    @Test
    public void 주문상태가식사인경우_빈테이블여부_변경_예외() throws Exception {
        TableRequest tableRequest = new TableRequest(5, false);
        TableResponse tableResponse = tableService.create(tableRequest);
        Menu menu = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(1000), null));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
        OrderRequest orderRequest = new OrderRequest(tableResponse.getId(), Arrays.asList(orderLineItemRequest));
        OrderResponse orderResponse = orderService.create(orderRequest);
        orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(OrderStatus.MEAL));

        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);
        assertThatThrownBy(() -> tableService.changeEmpty(tableResponse.getId(), tableEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 방문한 손님 수 변경")
    @Test
    public void 방문한손님수_변경_확인() throws Exception {
        //given
        TableRequest tableRequest = new TableRequest(5, false);
        TableResponse tableResponse = tableService.create(tableRequest);

        //when
        TableNumberOfGuestsRequest tableNumberOfGuestsRequest = new TableNumberOfGuestsRequest(2);
        TableResponse changeNumberOfGuests = tableService.changeNumberOfGuests(tableResponse.getId(),
                tableNumberOfGuestsRequest);

        //then
        assertThat(changeNumberOfGuests.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("주문테이블 방문한 손님 수 변경 예외 - 주문테이블이 존재하지 않는 경우")
    @Test
    public void 주문테이블이존재하지않는경우_방문한손님수변경_예외() throws Exception {
        //when
        //then
        TableNumberOfGuestsRequest tableNumberOfGuestsRequest = new TableNumberOfGuestsRequest(2);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, tableNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 방문한 손님 수 변경 예외 - 방문한 손님 수가 음수인 경우")
    @Test
    public void 방문한손님수가음수인경우_방문한손님수변경_예외() throws Exception {
        //given
        TableRequest tableRequest = new TableRequest(5, false);
        TableResponse tableResponse = tableService.create(tableRequest);

        //when
        //then
        TableNumberOfGuestsRequest tableNumberOfGuestsRequest = new TableNumberOfGuestsRequest(-1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableResponse.getId(), tableNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 방문한 손님 수 변경 예외 - 주문테이블이 빈테이블인 경우")
    @Test
    public void 주문테이블이빈테이블인경우_방문한손님수변경_예외() throws Exception {
        //given
        TableRequest tableRequest = new TableRequest(5, true);
        TableResponse tableResponse = tableService.create(tableRequest);

        //when
        //then
        TableNumberOfGuestsRequest tableNumberOfGuestsRequest = new TableNumberOfGuestsRequest(2);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableResponse.getId(), tableNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
