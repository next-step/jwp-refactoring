package kichenpos.table.table.domain;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 쿼리 서비스")
@ExtendWith(MockitoExtension.class)
class TableQueryServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableQueryService queryService;

    @Test
    @DisplayName("테이블들 데이터 가져오기")
    void findAll() {
        //when
        queryService.findAll();

        //then
        verify(orderTableRepository, only()).findAll();
    }
}
