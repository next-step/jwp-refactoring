package kitchenpos.table;


import kitchenpos.application.TableService;
import kitchenpos.common.ControllerTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.TableRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = TableRestController.class)
public class TableControllerTest extends ControllerTest<OrderTable> {

    private static final String BASE_URI = "/api/tables";

    @MockBean
    private TableService tableService;

    @Autowired
    private TableRestController tableRestController;

    private OrderTable 첫번째_테이블;

    @Override
    protected Object controller() {
        return tableRestController;
    }

    @BeforeEach
    void 사전준비() {
        첫번째_테이블 = new OrderTable();
        첫번째_테이블.setId(1L);
    }

    @DisplayName("주문테이블 생성요청")
    @Test
    void 주문테이블_생성요청() throws Exception {
        //Given
        when(tableService.create(any())).thenReturn(첫번째_테이블);

        //When
        ResultActions 결과 = postRequest(BASE_URI, 첫번째_테이블);

        //Then
        생성성공(결과, 첫번째_테이블);
    }

    @DisplayName("주문테이블 목록 조회요청")
    @Test
    void 주문테이블_목록_조회요청() throws Exception {
        //Given
        List<OrderTable> 주문테이블_목록 = new ArrayList<>(Arrays.asList(첫번째_테이블));
        when(tableService.list()).thenReturn(주문테이블_목록);

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        목록_조회성공(결과, 주문테이블_목록);
    }

    @DisplayName("주문테이블 비어있음 여부 수정요청")
    @Test
    void 주문테이블_비어있음_수정요청() throws Exception {
        //Given
        OrderTable 테이블_리퀘스트 = new OrderTable();
        테이블_리퀘스트.setId(첫번째_테이블.getId());
        테이블_리퀘스트.setEmpty(true);
        when(tableService.changeEmpty(첫번째_테이블.getId(), 테이블_리퀘스트)).thenReturn(테이블_리퀘스트);

        String 수정요청_URI = BASE_URI + "/" + 첫번째_테이블.getId() + "/empty";

        //When
        ResultActions 결과 = putRequest(수정요청_URI, 테이블_리퀘스트);

        //Then
        수정성공(결과, 테이블_리퀘스트);
    }

    @DisplayName("주문테이블 손님수 수정요청")
    @Test
    void 주문테이블_손님수_수정요청() throws Exception {
        //Given
        OrderTable 테이블_리퀘스트 = new OrderTable();
        테이블_리퀘스트.setId(첫번째_테이블.getId());
        테이블_리퀘스트.setNumberOfGuests(3);
        when(tableService.changeNumberOfGuests(첫번째_테이블.getId(), 테이블_리퀘스트)).thenReturn(테이블_리퀘스트);

        String 수정요청_URI = BASE_URI + "/" + 첫번째_테이블.getId() + "/number-of-guests";

        //When
        ResultActions 결과 = putRequest(수정요청_URI, 테이블_리퀘스트);

        //Then
        수정성공(결과, 테이블_리퀘스트);
    }
}
