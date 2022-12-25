package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.exception.NoSuchDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.OrderTableFixture.*;
import static kitchenpos.domain.TableGroupFixture.테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@DisplayName("테이블 테스트")
public class TableServiceTest {

    @MockBean
    private OrderTableRepository orderTableRepository;
    @Autowired
    private ApplicationEventPublisher publisher;
    private TableService tableService;

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;
    private OrderTable 테이블4;

    private TableGroup 테이블그룹;

    @BeforeEach
    void setup() {
        테이블그룹 = 테이블그룹(1L);

        테이블1 = 테이블(1L);
        테이블2 = 주문테이블(2L, null, 4, false);
        테이블3 = 빈주문테이블(3L);
        테이블4 = 주문테이블(4L, 테이블그룹, 4, false);

        tableService = new TableService(orderTableRepository, publisher);
    }

    @DisplayName("테이블을 생성한다")
    @Test
    void 테이블_생성() {
        // given
        given(orderTableRepository.save(any())).willReturn(테이블1);

        // when
        OrderTableRequest orderTableRequest = OrderTableRequest.of(테이블1);
        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        // then
        assertAll(
                () -> assertThat(orderTableResponse.getId()).isEqualTo(1L),
                () -> assertThat(orderTableResponse.getTableGroup()).isNull()
        );
    }

    @DisplayName("전체 테이블 목록을 조회한다")
    @Test
    void 전체_테이블_목록_조회() {
        // given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));

        // when
        List<OrderTableResponse> tables = tableService.list();

        // then
        assertAll(
                () -> assertThat(tables).hasSize(3),
                () -> assertThat(tables.stream()
                        .filter(orderTable -> orderTable.getId().equals(테이블2.getId()))
                        .findAny()
                        .get().getNumberOfGuests()).isEqualTo(4)
        );
    }

    @DisplayName("빈 테이블 여부 값을 갱신한다")
    @Test
    void 빈_테이블_여부_값_갱신() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블2));

        // when
        OrderTableRequest orderTableRequest = new OrderTableRequest(null, 0, true);
        OrderTableResponse orderTableResponse = tableService.changeEmpty(테이블3.getId(), orderTableRequest);

        // then
        assertThat(orderTableResponse.isEmpty()).isTrue();
    }

    @DisplayName("방문한 손님 수 값을 갱신한다")
    @Test
    void 방문한_손님_수_값_갱신() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블4));

        // when
        OrderTableRequest orderTableRequest = new OrderTableRequest(null, 5, false);
        OrderTableResponse orderTable = tableService.changeNumberOfGuests(테이블4.getId(), orderTableRequest);

        // then
        assertAll(
                () -> assertThat(테이블4.isEmpty()).isFalse(),
                () -> assertThat(테이블4.getNumberOfGuests()).isEqualTo(5)
        );
    }

    @DisplayName("존재하지 않는 테이블의 빈 테이블 여부 값을 갱신한다")
    @Test
    void 존재하지_않는_테이블의_빈_테이블_여부_값_갱신() {
        // given
        given(orderTableRepository.findById(테이블3.getId())).willReturn(Optional.ofNullable(null));

        // when & then
        OrderTableRequest orderTableRequest = new OrderTableRequest(null, 5, false);
        assertThatThrownBy(
                () -> tableService.changeEmpty(테이블3.getId(), orderTableRequest)
        ).isInstanceOf(NoSuchDataException.class);
    }

    @DisplayName("테이블그룹이 지정된 테이블의 빈 테이블 여부 값을 갱신한다")
    @Test
    void 테이블그룹_지정된_테이블_빈_테이블_여부_값_갱신() {
        // given
        given(orderTableRepository.findById(테이블4.getId())).willReturn(Optional.ofNullable(테이블4));

        // when & then
        OrderTableRequest orderTableRequest = OrderTableRequest.of(테이블4);

        assertThatThrownBy(
                () -> tableService.changeEmpty(테이블4.getId(), orderTableRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("음수인 방문한 손님 수를 갱신한다")
    @Test
    void 음수인_방문한_손님_수_갱신() {
        // when & then
        OrderTable 테이블5 = 주문테이블(5L, null, -5, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블5));

        OrderTableRequest orderTableRequest = new OrderTableRequest(null, 테이블5.getNumberOfGuests(), 테이블5.isEmpty());

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(테이블3.getId(), orderTableRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블의 방문한 손님 수를 갱신한다")
    @Test
    void 존재하지_않는_테이블의_방문한_손님_수_갱신() {
        // given
        given(orderTableRepository.findById(테이블3.getId())).willReturn(Optional.ofNullable(null));

        // when & then
        OrderTableRequest orderTableRequest = new OrderTableRequest(null, 7, false);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(테이블3.getId(), orderTableRequest)
        ).isInstanceOf(NoSuchDataException.class);
    }

    @DisplayName("빈 테이블의 방문한 손님 수를 갱신한다")
    @Test
    void 빈_테이블의_방문한_손님_수_갱신() {
        // give
        given(orderTableRepository.findById(테이블3.getId())).willReturn(Optional.ofNullable(테이블3));

        // when & then
        OrderTableRequest orderTableRequest = new OrderTableRequest(null, 7, true);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(테이블3.getId(), orderTableRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
