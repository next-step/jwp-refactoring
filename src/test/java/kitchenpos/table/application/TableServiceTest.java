package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableEmptyUpdateRequest;
import kitchenpos.table.dto.TableGuestsUpdateRequest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

	@InjectMocks
	private TableService tableService;
	@Mock
	private OrderTableRepository orderTableRepository;
	@Mock
	private OrderRepository orderRepository;

	private Menu 후라이드둘세트;
	private OrderTable 테이블;
	private OrderTable 빈_테이블;

	@BeforeEach
	void setup() {
		// given
		Product 후라이드치킨 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(17_000));
		MenuGroup 추천메뉴 = MenuGroup.of(1L, "추천메뉴");
		MenuProduct 메뉴_상품 = MenuProduct.of(후라이드치킨.getId(), 2L);

		후라이드둘세트 = Menu.of(1L, "후라이드 둘 세트", BigDecimal.valueOf(32_000), 추천메뉴);
		후라이드둘세트.addMenuProducts(Collections.singletonList(메뉴_상품));
		테이블 = OrderTable.of(1L, 4, false);
		빈_테이블 = OrderTable.of(2L, 4, true);

	}

	@DisplayName("테이블을 생성한다")
	@Test
	void createTest() {
		// given
		TableRequest request = new TableRequest(빈_테이블.getNumberOfGuests().toInt(), 빈_테이블.isEmpty());
		given(orderTableRepository.save(any())).willReturn(빈_테이블);

		// when
		TableResponse response = tableService.create(request);

		// then
		assertAll(
			() -> assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
			() -> assertThat(response.isEmpty()).isEqualTo(request.isEmpty())
		);
	}

	@DisplayName("주문 테이블 목록을 조회한다")
	@Test
	void listTest() {
		// given
		given(orderTableRepository.findAll()).willReturn(Collections.singletonList(테이블));

		// when
		List<TableResponse> result = tableService.list();

		// then
		assertThat(result.size()).isEqualTo(1);
	}

	@DisplayName("주문 테이블을 빈 테이블로 변경한다")
	@Test
	void changeEmptyTest() {
		// given
		TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(false);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(테이블));
		given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

		// when
		TableResponse result = tableService.changeEmpty(테이블.getId(), request);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@DisplayName("빈 테이블 변경 시, 테이블 그룹에 속해있지 않아야 한다")
	@Test
	void changeEmptyTest2() {
		// given
		TableGroup 테이블_그룹 = TableGroup.of(1L, Arrays.asList(테이블, 테이블));
		OrderTable 속한_테이블 = OrderTable.of(1L, 테이블_그룹, NumberOfGuests.of(4), false);
		TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(false);
		Long requestId = 속한_테이블.getId();
		given(orderTableRepository.findById(any())).willReturn(Optional.of(속한_테이블));

		// when, then
		assertThatThrownBy(() -> tableService.changeEmpty(requestId, request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("빈 테이블 변경 시, 조리 중이거나 식사 중인 테이블이면 안된다")
	@Test
	void changeEmptyTest3() {
		// given
		Long requestId = 빈_테이블.getId();
		TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(false);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(빈_테이블));
		given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

		// when, then
		assertThatThrownBy(() -> tableService.changeEmpty(requestId, request))
			.isInstanceOf(AppException.class);
	}

	@DisplayName("주문 테이블 손님 수 변경 시, 손님수가 0명 이상이어야 한다")
	@Test
	void changeNumberOfGuests() {
		// given
		Long requestId = 테이블.getId();
		TableGuestsUpdateRequest request = new TableGuestsUpdateRequest(-1);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(테이블));

		// when, then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(requestId, request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("주문 테이블 손님 수 변경 시, 빈 테이블 상태가 아니어야 한다")
	@Test
	void changeNumberOfGuests2() {
		// given
		Long requestId = 빈_테이블.getId();
		TableGuestsUpdateRequest request = new TableGuestsUpdateRequest(-1);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(빈_테이블));

		// when, then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(requestId, request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("주문 테이블 손님 수를 변경한다")
	@Test
	void changeNumberOfGuests3() {
		// given
		TableGuestsUpdateRequest request = new TableGuestsUpdateRequest(테이블.getNumberOfGuests().toInt());
		given(orderTableRepository.findById(any())).willReturn(Optional.of(테이블));

		// when
		TableResponse result = tableService.changeNumberOfGuests(테이블.getId(), request);

		// then
		assertThat(result.getNumberOfGuests()).isEqualTo(테이블.getNumberOfGuests().toInt());
	}

}
