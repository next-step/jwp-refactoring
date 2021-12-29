package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderTableRepositoryTest {

	@Autowired
	private OrderTableRepository orderTableRepository;

	@Test
	@DisplayName("주문테이블 저장 테스트")
	public void saveOrderTableTest() {
		//given
		OrderTable orderTable = new OrderTable(0, true);

		//when
		OrderTable save = orderTableRepository.save(orderTable);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(10L);
	}

	@Test
	@DisplayName("주문테이블 목록 조회 테스트")
	public void findAllOrderTableTest() {
		//when
		List<OrderTable> orderTables = orderTableRepository.findAll();

		//then
		assertThat(orderTables).hasSizeGreaterThanOrEqualTo(8);
	}

	@Test
	@DisplayName("주문테이블 id로 조회 테스트")
	public void findByIdOrderTableTest() {
		//when
		OrderTable orderTable = orderTableRepository.findById(1L).orElse(new OrderTable());

		//then
		assertThat(orderTable.getId()).isEqualTo(1);
	}

	@Test
	@DisplayName("없는 주문테이블 id로 조회 테스트")
	public void findByIdOrderTableFailTest() {
		//when
		OrderTable orderTable = orderTableRepository.findById(99L).orElse(new OrderTable());

		//then
		assertThat(orderTable.getId()).isNull();
	}
}
