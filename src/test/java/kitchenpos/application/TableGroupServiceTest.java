package kitchenpos.application;

import kitchenpos.domain.OrderLineItemBag;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableBag;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.application.TableServiceTest.주문_테이블;
import static kitchenpos.domain.OrderTableTest.두_명의_방문객;
import static kitchenpos.domain.OrderTableTest.빈_상태;
import static kitchenpos.domain.OrderTest.조리_상태;
import static kitchenpos.domain.OrderTest.주문;
import static kitchenpos.domain.TableGroupTest.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

@Transactional
@SpringBootTest
@DisplayName("단체 지정 테스트")
public class TableGroupServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final List<OrderTable> 주문_테이블_목록 = Arrays.asList(
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태)),
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태)));

        final TableGroup 단체_지정_테이블 = 단체_지정(LocalDateTime.now(), OrderTableBag.from(주문_테이블_목록));

        assertThat(tableGroupService.create(단체_지정_테이블).getOrderTableBag().getOrderTableList()).hasSize(주문_테이블_목록.size());
    }

    @DisplayName("생성 예외 - 주문 테이블 목록이 비어있는 경우")
    @Test
    void 생성_예외_주문_테이블_목록이_비어_있는_경우() {

        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(단체_지정(LocalDateTime.now(), OrderTableBag.from(Collections.emptyList()))));
    }

    @DisplayName("생성 예외 - 주문 테이블이 한개 이하인 경우")
    @Test
    void 생성_예외_주문_테이블이_한개_이하인_경우() {

        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(단체_지정(LocalDateTime.now(), OrderTableBag.from(Arrays.asList(
                        tableService.create(주문_테이블(두_명의_방문객, 빈_상태)))))));
    }

    @DisplayName("생성 예외 - 주문 테이블의 수가 실제 저장된 주문 테이블의 수와 일치하지 않는 경우")
    @Test
    void 생성_예외_주문_테이블의_수가_실제_저장된_주문_테이블_수와_일치하지_않는_경우() {
        //given:
        final TableGroup 단체_지정_테이블 = 단체_지정(LocalDateTime.now(), OrderTableBag.from(Arrays.asList(
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태)),
                주문_테이블(두_명의_방문객, 빈_상태))
        ));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(단체_지정_테이블));
    }

    @DisplayName("생성 예외 - 이미 단체 지정이 되어 있는 주문 테이블이 포함 된 경우")
    @Test
    void 생성_예외_이미_단체_지정이_되어_있는_주문_테이블이_포함_된_경우() {
        //given:
        final OrderTable 첫_번째_테이블 = tableService.create(주문_테이블(두_명의_방문객, 빈_상태));
        final OrderTable 두_번째_테이블 = tableService.create(주문_테이블(두_명의_방문객, 빈_상태));

        tableGroupService.create(단체_지정(LocalDateTime.now(), OrderTableBag.from(Arrays.asList(첫_번째_테이블, 두_번째_테이블))));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(단체_지정(LocalDateTime.now(), OrderTableBag.from(Arrays.asList(첫_번째_테이블, 두_번째_테이블)))));
    }

    @DisplayName("단체 지정 해제 성공")
    @Test
    void 단체_지정_해제_성공() {
        //given:
        final TableGroup 단체_지정_테이블 = tableGroupService.create(단체_지정(LocalDateTime.now(), OrderTableBag.from(new ArrayList<>(Arrays.asList(
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태)),
                tableService.create(주문_테이블(두_명의_방문객, 빈_상태)))
        ))));
        //when,then:
        assertThatNoException().isThrownBy(() -> tableGroupService.ungroup(단체_지정_테이블.getId()));
    }

    @DisplayName("단체 지정 해제 예외 - 주문 상태가 결제 완료 상태가 아닌 경우")
    @Test
    void 단체_지정_해제_예외_주문_상태가_결제_완료_상태가_아닌_경우() {
        //given:
        final OrderTable 첫_번째_테이블 = tableService.create(주문_테이블(두_명의_방문객, 빈_상태));
        final OrderTable 두_번째_테이블 = tableService.create(주문_테이블(두_명의_방문객, 빈_상태));

        orderRepository.save(주문(
                첫_번째_테이블,
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Collections.emptyList())));
        //when:
        final TableGroup 단체_지정_테이블 = tableGroupService.create(단체_지정(LocalDateTime.now(), OrderTableBag.from(Arrays.asList(
                첫_번째_테이블, 두_번째_테이블
        ))));
        //then:
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(단체_지정_테이블.getId()));
    }
}
