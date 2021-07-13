package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.order.OrderServiceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.TableException;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.TableGroup;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

	@Mock
	private TableRepository tableRepository;

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private TableService tableService;

	MenuGroup 치킨;
	Menu 양념반_후라이드반;
	Product 양념치킨;
	Product 후라이드치킨;
	MenuProduct 양념_반_치킨;
	MenuProduct 후라이드_반_치킨;

	Order 주문;
	OrderLineItem 주문항목1;
	OrderLineItem 주문항목2;
	OrderLineItems 주문항목들;
	OrderTable 주문테이블;

	@BeforeEach
	void setUp() {
		치킨 = new MenuGroup(1L, "치킨");
		양념치킨 = new Product(1L, "양념치킨", new Price(BigDecimal.valueOf(1000)));
		후라이드치킨 = new Product(2L, "후라이드치킨", new Price(BigDecimal.valueOf(1000)));
		양념_반_치킨 = new MenuProduct(1L, 양념치킨, new Quantity(1));
		후라이드_반_치킨 = new MenuProduct(2L, 후라이드치킨, new Quantity(1));
		양념반_후라이드반 = new Menu(1L, "양념반 후라이드반", new Price(BigDecimal.valueOf(2000)), 치킨,
			new MenuProducts(Arrays.asList(양념_반_치킨, 후라이드_반_치킨)));

		주문항목1 = OrderServiceTest.주문항목생성(양념반_후라이드반, new Quantity(1), 1L);
		주문항목2 = OrderServiceTest.주문항목생성(양념반_후라이드반, new Quantity(1), 2L);
		주문테이블 = 주문테이블생성(1L, new NumberOfGuests(1), false);
		주문항목들 = new OrderLineItems(Arrays.asList(주문항목1, 주문항목2));
		주문 = OrderServiceTest.주문생성(1L, 주문테이블, 주문항목들);

	}

	@DisplayName("주문 테이블을 생성한다.")
	@Test
	void 주문_테이블_생성() {
		OrderTableRequest 주문테이블_생성_요청 = new OrderTableRequest(null, 1);
		given(tableRepository.save(any())).willReturn(주문테이블);

		OrderTableResponse created = tableService.create(주문테이블_생성_요청);

		주문테이블_생성_확인(created, 주문테이블);
	}

	@DisplayName("주문 테이블 리스트 조회한다.")
	@Test
	void 주문_테이블_리스트_조회() {
		given(tableRepository.findAll()).willReturn(Arrays.asList(주문테이블));

		List<OrderTableResponse> created = tableService.list();

		주문테이블_리스트_조회_확인(created);
	}

	@DisplayName("주문 테이블을 비우거나 채운다.")
	@Test
	void 주문_테이블을_비우거나_채운다() {
		OrderTableChangeEmptyRequest 주문_테이블_비움_요청 = new OrderTableChangeEmptyRequest(true);
		주문.changeOrderStatus(OrderStatus.COMPLETION);
		주문테이블.changeEmpty(true, 주문);
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
		given(orderRepository.findByOrderTableId(주문테이블.getId())).willReturn(주문);
		given(tableRepository.save(주문테이블)).willReturn(주문테이블);

		OrderTable changed = tableService.changeEmpty(주문테이블.getId(), 주문_테이블_비움_요청);

		주문테이블_비우거나_채움_확인(changed, 주문테이블);
	}

	@DisplayName("주문 테이블을 비우거나 채울 때 테이블이 존재하지 않으면 할 수 없다")
	@Test
	void 주문_테이블을_비우거나_채울_때_테이블이_존재하지_않으면_할_수_없다() {
		OrderTableChangeEmptyRequest 주문_테이블_비움_요청 = new OrderTableChangeEmptyRequest(true);
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			tableService.changeEmpty(주문테이블.getId(), 주문_테이블_비움_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블을 비우거나 채울 때 주문 테이블이 단체 지정에 속하면 비울 수 없다")
	@Test
	void 주문_테이블을_비우거나_채울_때_주문_테이블이_단체_지정에_속하면_비울_수_없다() {
		OrderTableChangeEmptyRequest 주문_테이블_비움_요청 = new OrderTableChangeEmptyRequest(true);

		OrderTable 주문테이블1번 = 주문테이블생성(1L, new NumberOfGuests(1), true);
		OrderTable 주문테이블2번 = 주문테이블생성(2L, new NumberOfGuests(1), true);
		TableGroup 단체지정 = new TableGroup(1L, Arrays.asList(주문테이블1번, 주문테이블2번));

		given(tableRepository.findById(주문테이블1번.getId())).willReturn(Optional.of(주문테이블1번));

		assertThatThrownBy(() -> {
			tableService.changeEmpty(주문테이블.getId(), 주문_테이블_비움_요청);
		}).isInstanceOf(TableException.class);
	}

	@DisplayName("주문 테이블을 비우거나 채울 때 주문 테이블들이 조리중이거나 식사중일 경우 비울 수 없다.")
	@Test
	void 주문_테이블을_비우거나_채울_때_주문_테이블들이_조리중이거나_식사중일_경우_비울_수_없다() {
		OrderTableChangeEmptyRequest 주문_테이블_비움_요청 = new OrderTableChangeEmptyRequest(true);
		주문.changeOrderStatus(OrderStatus.MEAL);
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
		given(orderRepository.findByOrderTableId(주문테이블.getId())).willReturn(주문);

		assertThatThrownBy(() -> {
			tableService.changeEmpty(주문테이블.getId(), 주문_테이블_비움_요청);
		}).isInstanceOf(TableException.class);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다")
	@Test
	void 주문_테이블의_손님_수_변경() {
		OrderTableRequest 주문테이블_생성_요청 = new OrderTableRequest(null, 4);
		주문테이블.changeNumberOfGuests(new NumberOfGuests(4));
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
		given(tableRepository.save(주문테이블)).willReturn(주문테이블);

		OrderTable changedNumberOfGuests = tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블_생성_요청);

		주문_테이블의_손님_수_변경_확인(changedNumberOfGuests.getNumberOfGuests().value(), 4);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다 - 변경할 손님 수가 0보다 작으면 변경할 수 없다")
	@Test
	void 주문_테이블의_손님_수_변경_변경할_손님_수가_0보다_작으면_변경할_수_없다() {
		OrderTableRequest 주문테이블_생성_요청 = new OrderTableRequest(null, -1);
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블_생성_요청);
		}).isInstanceOf(TableException.class);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다 - 변경할 주문 테이블이 존재하지 않으면 변경할 수 없다.")
	@Test
	void 주문_테이블의_손님_수_변경_변경할_주문_테이블이_존재하지_않으면_변경할_수_없다() {
		OrderTableRequest 주문테이블_생성_요청 = new OrderTableRequest(null, -1);
		주문테이블.changeNumberOfGuests(new NumberOfGuests(4));
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블_생성_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다 - 변경할 주문 테이블이 비워져 있다면 변경할 수 없다.")
	@Test
	void 주문_테이블의_손님_수_변경_변경할_주문_테이블이_비워져_있다면_변경할_수_없다() {
		OrderTableRequest 주문테이블_생성_요청 = new OrderTableRequest(null, 4);
		주문.changeOrderStatus(OrderStatus.COMPLETION);
		주문테이블.changeNumberOfGuests(new NumberOfGuests(4));
		주문테이블.changeEmpty(true, 주문);

		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));

		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블_생성_요청);
		}).isInstanceOf(TableException.class);
	}

	private void 주문_테이블의_손님_수_변경_확인(int changedNumberOfGuests, int expected) {
		assertThat(changedNumberOfGuests).isEqualTo(expected);
	}

	private void 주문테이블_비우거나_채움_확인(OrderTable changed, OrderTable expected) {
		assertThat(changed.isEmpty()).isEqualTo(expected.isEmpty());
	}

	private void 주문테이블_리스트_조회_확인(List<OrderTableResponse> created) {
		assertThat(created).isNotNull();
		assertThat(created).isNotEmpty();
	}

	private void 주문테이블_생성_확인(OrderTableResponse created, OrderTable expected) {
		assertThat(created.getId()).isEqualTo(expected.getId());
		assertThat(created.isEmpty()).isEqualTo(expected.isEmpty());
		assertThat(created.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests().value());
	}

	public static OrderTable 주문테이블생성(Long id, NumberOfGuests numberOfGuests) {
		OrderTable orderTable = new OrderTable(id, numberOfGuests);
		return orderTable;
	}

	public static OrderTable 주문테이블생성(Long id, NumberOfGuests numberOfGuests, boolean isEmpty) {
		OrderTable orderTable = new OrderTable(id, numberOfGuests, isEmpty);
		return orderTable;
	}


}
