package kitchenpos.table.application;

import kitchenpos.OrderApplication;
import kitchenpos.OrderTestSupport;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableEmptyRequest;
import kitchenpos.table.dto.TableNumberOfGuestsRequest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = OrderApplication.class)
@DisplayName("테이블 서비스 통합 테스트")
@Transactional
@SpringBootTest
public class TableServiceTest extends OrderTestSupport {
    @Autowired
    private TableService tableService;

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
        OrderTable orderTable = 테이블_등록되어있음(5, false);
        Menu menu = 메뉴_등록되어있음("메뉴이름", BigDecimal.valueOf(1000));
        Order order = 주문_등록되어있음(orderTable, Arrays.asList(new OrderLineItem(menu.getId(), 1L)));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);
        TableResponse changeEmptyTableResponse = tableService.changeEmpty(orderTable.getId(), tableEmptyRequest);

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
                .hasMessage("주문테이블이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 빈테이블 여부 변경 예외 - 주문상태가 조리나 식사인 경우")
    @Test
    public void 주문상태가조리나식사인경우_빈테이블여부_변경_예외() throws Exception {
        //given
        OrderTable orderTable = 테이블_등록되어있음(5, false);
        Menu menu = 메뉴_등록되어있음("메뉴이름", BigDecimal.valueOf(1000));
        주문_등록되어있음(orderTable, Arrays.asList(new OrderLineItem(menu.getId(), 1L)));

        //when
        //then
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), tableEmptyRequest))
                .hasMessage("주문테이블의 주문상태가 조리나 식사입니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 빈테이블 여부 변경 예외 - 단체지정이 되어 있는 경우")
    @Test
    public void 단체지정이되어있는경우_빈테이블여부_변경_예외() throws Exception {
        //given
        OrderTable orderTable1 = 테이블_등록되어있음(5, true);
        OrderTable orderTable2 = 테이블_등록되어있음(5, true);
        단체지정_등록되어있음(Arrays.asList(orderTable1, orderTable2));
        Menu menu = 메뉴_등록되어있음("메뉴", BigDecimal.valueOf(1000));
        Order order = 주문_등록되어있음(orderTable1, Arrays.asList(new OrderLineItem(menu.getId(), 1L)));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when
        //then
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), tableEmptyRequest))
                .hasMessage("단체지정이 되어있으면 안됩니다.")
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
                .hasMessage("주문테이블이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}
