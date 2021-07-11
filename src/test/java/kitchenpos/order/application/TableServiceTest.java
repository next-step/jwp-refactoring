package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.TableEmptyRequest;
import kitchenpos.order.dto.TableNumberOfGuestsRequest;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
                .hasMessage("주문테이블이 존재하지 않습니다.")
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
