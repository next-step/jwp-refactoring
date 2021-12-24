package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블을 등록할 수 있다")
    @Test
    void 테이블_등록() {
        // given
        OrderTable 테이블 = new OrderTable();
        테이블.setId(1L);
        테이블.setNumberOfGuests(3);
        
        given(orderTableRepository.save(테이블)).willReturn(테이블);

        // when
        OrderTable 저장된_테이블 = tableService.create(테이블);

        // then
        assertThat(저장된_테이블).isEqualTo(테이블);
    }
    
    @DisplayName("테이블 목록을 조회할 수 있다")
    @Test
    void 테이블_목록_조회() {
        // given
        OrderTable 첫번째_테이블 = new OrderTable();
        첫번째_테이블.setId(1L);
        첫번째_테이블.setNumberOfGuests(3);
        첫번째_테이블.setEmpty(false);
        
        OrderTable 두번째_테이블 = new OrderTable();
        두번째_테이블.setId(1L);
        두번째_테이블.setNumberOfGuests(5);
        두번째_테이블.setEmpty(false);
        
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(첫번째_테이블, 두번째_테이블));
    
        // when
        List<OrderTable> 테이블_목록 = tableService.list();
    
        // then
        assertThat(테이블_목록).containsExactly(첫번째_테이블, 두번째_테이블);
    }
    
    @DisplayName("테이블을 빈 테이블로 변경 할 수 있다")
    @Test
    void 테이블_상태_변경() {
        // given
        OrderTable 테이블 = new OrderTable();
        테이블.setId(1L);
        테이블.setNumberOfGuests(3);
        테이블.setEmpty(false);
        
        OrderTable 빈_테이블 = new OrderTable();
        빈_테이블.setId(1L);
        빈_테이블.setNumberOfGuests(3);
        빈_테이블.setEmpty(true);
        
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableRepository.save(테이블)).willReturn(빈_테이블);
    
        // when
        OrderTable 상태_변경후_테이블 = tableService.changeEmpty(테이블.getId(), 빈_테이블);
    
        // then
        assertThat(상태_변경후_테이블.isEmpty()).isTrue();
    }
    
    @DisplayName("등록된 테이블만 빈 테이블로 변경할 수 있다 - 예외처리")
    @Test
    void 등록되지않은_테이블_빈_테이블_변경_불가() {
        // given
        TableGroup 단체지정 = new TableGroup();
        단체지정.setId(1L);
        
        OrderTable 등록되지_않은_테이블 = new OrderTable();
        등록되지_않은_테이블.setId(1L);
        등록되지_않은_테이블.setTableGroup(단체지정);
        등록되지_않은_테이블.setNumberOfGuests(3);
        등록되지_않은_테이블.setEmpty(false);
        
        // when
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(등록되지_않은_테이블.getId(), 등록되지_않은_테이블);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록된 테이블만 빈 테이블로 변경할 수 있습니다");
    
    }
    
    @DisplayName("단체지정이 된 테이블은 빈 테이블로 변경할 수 없다 - 예외처리")
    @Test
    void 단체지정_테이블_빈_테이블_변경_불가() {
        // given
        TableGroup 단체지정 = new TableGroup();
        단체지정.setId(1L);
        
        OrderTable 테이블 = new OrderTable();
        테이블.setId(1L);
        테이블.setTableGroup(단체지정);
        테이블.setNumberOfGuests(3);
        테이블.setEmpty(false);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블));
        
        // when, then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(테이블.getId(), 테이블);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("단체지정이 되어있는 테이블은 빈 테이블로 변경할 수 없습니다");
    
    }
    
    @DisplayName("조리중이거나 식사중인 테이블은 빈 테이블로 변경할 수 없다 - 예외처리")
    @Test
    void 조리중_식사중_테이블은_빈_테이블_변경_불가() {
        // given
        OrderTable 테이블 = new OrderTable();
        테이블.setId(1L);
        테이블.setNumberOfGuests(3);
        테이블.setEmpty(false);
        
        OrderTable 빈_테이블 = new OrderTable();
        빈_테이블.setId(1L);
        빈_테이블.setNumberOfGuests(3);
        빈_테이블.setEmpty(true);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블));
    
        // when
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);
        
        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(테이블.getId(), 빈_테이블);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("조리중이거나 식사중인 테이블은 빈 테이블로 변경할 수 없습니다");
    
    }
    
    @DisplayName("테이블의 방문한 손님 수를 변경할 수 있다")
    @Test
    void 테이블_방문_손님_수_변경() {
        // given
        OrderTable 테이블 = new OrderTable();
        테이블.setId(1L);
        테이블.setNumberOfGuests(5);
        
        OrderTable 손님_수_변경_테이블 = new OrderTable();
        손님_수_변경_테이블.setId(1L);
        손님_수_변경_테이블.setNumberOfGuests(3);
        
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블));
        given(orderTableRepository.save(테이블)).willReturn(손님_수_변경_테이블);
    
        // when
        OrderTable 손님_수_변경후_테이블 = tableService.changeNumberOfGuests(테이블.getId(), 손님_수_변경_테이블);
    
        // then
        assertThat(손님_수_변경후_테이블.getNumberOfGuests()).isEqualTo(손님_수_변경_테이블.getNumberOfGuests());
    }
    
    @DisplayName("테이블의 손님 수는 0명 이상이어야한다 - 예외처리")
    @Test
    void 테이블_손님_수_0명_이상() {
        // given
        OrderTable 테이블 = new OrderTable();
        테이블.setId(1L);
        테이블.setNumberOfGuests(-2);
        
        // when, then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(테이블.getId(), 테이블);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("테이블의 손님 수는 최소 0명 이상이어야합니다");
    
    }
    
    @DisplayName("등록된 테이블만 방문 손님 수를 지정할 수 있다 - 예외처리")
    @Test
    void 등록된_테이블만_방문_손님_수_변경() {
        // given
        OrderTable 등록되지_않은_테이블 = new OrderTable();
        등록되지_않은_테이블.setId(1L);
        등록되지_않은_테이블.setNumberOfGuests(3);
        
        // when
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(등록되지_않은_테이블.getId(), 등록되지_않은_테이블);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록된 테이블만 방문 손님 수를 지정할 수 있습니다");
    
    }
}
