package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;

@Transactional
@SpringBootTest
class TableRestControllerTest {

	@Autowired
	private TableRestController tableRestController;

	@Test
	@DisplayName("테이블 생성 테스트")
	public void createTableTest() {
		//given
		OrderTable orderTable = new OrderTable(null, null, 0, true);
		//when
		ResponseEntity<OrderTable> responseEntity = tableRestController.create(orderTable);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getHeaders().getLocation().toString()).isEqualTo("/api/tables/10");
	}

	@Test
	@DisplayName("테이블 목록 조회 테스트")
	public void findAllTableTest() {
		//given
		//when
		ResponseEntity<List<OrderTable>> responseEntity = tableRestController.list();

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).hasSize(9);
	}


	@Test
	@DisplayName("테이블 빈 상태로 변경 테스트")
	public void changeEmptyTest() {
		//given
		OrderTable changeParamOrderTable = new OrderTable(null, null, 0, true);
		//when
		ResponseEntity<OrderTable> responseEntity = tableRestController.changeEmpty(3L, changeParamOrderTable);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody().isEmpty()).isTrue();
	}

	@Test
	@DisplayName("테이블 인원수 변경 테스트")
	public void changeNumberOfGuests() {
		//given
		OrderTable changeParamOrderTable = new OrderTable(null, null, 3, false);
		//when
		ResponseEntity<OrderTable> responseEntity = tableRestController.changeNumberOfGuests(4L,changeParamOrderTable);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody().getNumberOfGuests()).isEqualTo(3);
	}
}
