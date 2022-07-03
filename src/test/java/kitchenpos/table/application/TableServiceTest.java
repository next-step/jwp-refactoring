package kitchenpos.table.application;

import static kitchenpos.fixture.TableFixture.테이블_생성;
import static kitchenpos.fixture.TableFixture.테이블요청_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.validator.TableValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableValidator tableValidator;
    @InjectMocks
    private TableService tableService;

    private OrderTableRequest orderTableRequest;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTableRequest = 테이블요청_생성(1L, 5, false);
        orderTable = 테이블_생성(1L, 3, false);
    }

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        //given
        given(orderTableRepository.save(any())).willReturn(orderTable);

        //when
        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        //then
        assertThat(orderTableResponse).isNotNull();
        assertThat(orderTableResponse.getId()).isEqualTo(orderTable.getId());
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        //given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable));

        //when
        List<OrderTableResponse> orderTableResponses = tableService.list();

        //then
        assertThat(orderTableResponses).hasSize(1);
    }

    @DisplayName("테이블의 빈 테이블 여부를 변경한다.")
    @Test
    void changeEmpty() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(any())).willReturn(orderTable);

        //when
        OrderTableResponse orderTableResponse = tableService.changeEmpty(orderTable.getId(), orderTableRequest);

        //then
        assertThat(orderTableResponse).isNotNull();
        assertThat(orderTableResponse.getEmpty()).isEqualTo(orderTableRequest.getEmpty());
    }

    @DisplayName("존재하지 않는 테이블의 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_invalidEmptyTable() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹에 속해 있는 테이블의 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_invalidHasTableGroup() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(tableGroupRepository.existsByOrderTableId(any())).willReturn(true);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리 또는 식사중인 테이블의 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_invalidOrderStatus() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        willThrow(new IllegalArgumentException("현재 조리중이거나 식사중인 주문이 존재합니다."))
                .given(tableValidator)
                .validateOrderStatus(1L);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(any())).willReturn(orderTable);

        //when
        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(orderTable.getId(),
                orderTableRequest);

        //then
        assertThat(orderTableResponse).isNotNull();
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님 수를 0 미만으로 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_invalidMinusGuests() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        orderTableRequest = 테이블요청_생성(null, -1, false);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("손님 수는 0 미만일 수 없습니다.");
    }

    @DisplayName("존재하지 않는 테이블의 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_invalidNotExistsTable() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_invalidEmptyTable() {
        //given
        orderTable.changeEmpty(true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블의 손님의 수는 변경할 수 없습니다.");
    }

}
