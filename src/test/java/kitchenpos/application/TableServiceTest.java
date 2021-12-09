package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.application.fixture.Fixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        //given
        주문테이블등록_사전DB저장내용();
    }

    @DisplayName("주문테이블이 생성된다.")
    @Test
    void create_orderTable() {
        // when
        OrderTable createdOrderTable = tableService.create(Fixture.치킨_주문_단체테이블);

        // then
        Assertions.assertThat(createdOrderTable).isEqualTo(Fixture.치킨_주문_단체테이블);
    }

    @DisplayName("주문테이블이 조회된다.")
    @Test
    void search_orderTable() {
        주문테이블_조회전_DB내용();

        // when
        List<OrderTable> searchedOrderTable = tableService.list();

        // then
        Assertions.assertThat(searchedOrderTable).isEqualTo(List.of(Fixture.치킨_주문_단체테이블, Fixture.치킨2_주문_단체테이블));
    }

    //Todo Exception
    @DisplayName("주문테이블이 빈테이블 전환 여부가 변경된다.")
    @Test
    void update_orderTable_emptyStatus() {
        주문테이블_상태변경전_DB내용();
        
        // when
        OrderTable changedOrderTable = tableService.changeEmpty(Fixture.치킨_주문_개인테이블.getId(), Fixture.치킨_주문_개인테이블);

        // then
        Assertions.assertThat(changedOrderTable).isEqualTo(Fixture.치킨_주문_개인테이블);
    }

    @DisplayName("단체지정된 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_existOrderTableInTableGroup() {
        단체지정_테이블조회전_DB내용();

        // given
        Fixture.치킨_주문_단체테이블.setTableGroupId(Fixture.단체주문테이블.getId());

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableService.changeEmpty(Fixture.치킨_주문_단체테이블.getId(), Fixture.치킨_주문_단체테이블));
    }

    @DisplayName("주문상태가 계산완료가 아닌 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_EmptyStatus() {
        단체지정_테이블조회전_DB내용();
        
        // given
        Fixture.치킨_주문_단체테이블.setTableGroupId(Fixture.단체주문테이블.getId());

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                      .isThrownBy(() -> tableService.changeEmpty(Fixture.치킨_주문_단체테이블.getId(), Fixture.치킨_주문_단체테이블));
    }

    @DisplayName("주문테이블의 방문한 손님수가 변경된다.")
    @Test
    void update_orderTable_numberOfGuests() {
        단체지정_테이블조회전_DB내용();
        
        // given
        Fixture.치킨_주문_단체테이블.setNumberOfGuests(3);
        Fixture.치킨_주문_단체테이블.setEmpty(false);

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(Fixture.치킨_주문_단체테이블.getId(), Fixture.치킨_주문_단체테이블);

        // then
        Assertions.assertThat(changedOrderTable).isEqualTo(Fixture.치킨_주문_단체테이블);
    }

    @DisplayName("주문테이블의 방문한 손님수를 0이만으로 변경시 예외가 발생된다.")
    @ValueSource(ints = {-1, -9})
    @ParameterizedTest(name ="[{index}] 방문한 손님수는 [{0}]")
    void exception_updateOrderTable_underZeroCountAboutNumberOfGuest(int numberOfGuests) {
        단체지정_테이블조회전_DB내용();

        // given
        Fixture.치킨_주문_단체테이블.setNumberOfGuests(numberOfGuests);
        Fixture.치킨_주문_단체테이블.setEmpty(false);

        // when
        // then       
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableService.changeNumberOfGuests(Fixture.치킨_주문_단체테이블.getId(), Fixture.치킨_주문_단체테이블));

    }

    @DisplayName("빈테이블에 방문한 손님수 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_atEmptyTable() {
        단체지정_테이블조회전_DB내용();

        // given
        Fixture.치킨_주문_단체테이블.setNumberOfGuests(3);
        Fixture.치킨_주문_단체테이블.setEmpty(true);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                   .isThrownBy(() -> tableService.changeNumberOfGuests(Fixture.치킨_주문_단체테이블.getId(), Fixture.치킨_주문_단체테이블));
    }

    private void 단체지정_테이블조회전_DB내용() {
        when(orderTableDao.findById(Fixture.치킨_주문_단체테이블.getId())).thenReturn(Optional.of(Fixture.치킨_주문_단체테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);
    }


    private void 주문테이블_상태변경전_DB내용() {
        when(orderTableDao.findById(Fixture.치킨_주문_개인테이블.getId())).thenReturn(Optional.of(Fixture.치킨_주문_개인테이블));
        when(orderTableDao.save(Fixture.치킨_주문_개인테이블)).thenReturn(Fixture.치킨_주문_개인테이블);
    }

    private void 주문테이블_조회전_DB내용() {
        when(orderTableDao.findAll()).thenReturn(List.of(Fixture.치킨_주문_단체테이블, Fixture.치킨2_주문_단체테이블));
    }

    private void 주문테이블등록_사전DB저장내용() {
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(Fixture.치킨_주문_단체테이블);
    }
}
