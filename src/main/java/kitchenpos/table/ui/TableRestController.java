package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.TableEmptyUpdateRequest;
import kitchenpos.table.dto.TableGuestsUpdateRequest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.utils.ResponseUtils;

@RequestMapping(TableRestController.BASE_URL)
@RestController
public class TableRestController {
	private final TableService tableService;

	public static final String BASE_URL = "/api/tables";

	public TableRestController(final TableService tableService) {
		this.tableService = tableService;
	}

	@PostMapping
	public ResponseEntity<TableResponse> create(@RequestBody final TableRequest request) {
		final TableResponse created = tableService.create(request);
		final URI uri = ResponseUtils.createdUrl(BASE_URL, created.getId());
		return ResponseEntity.created(uri)
			.body(created)
			;
	}

	@GetMapping
	public ResponseEntity<List<TableResponse>> list() {
		return ResponseEntity.ok()
			.body(tableService.list())
			;
	}

	@PutMapping("/{orderTableId}/empty")
	public ResponseEntity<TableResponse> changeEmpty(
		@PathVariable final Long orderTableId,
		@RequestBody final TableEmptyUpdateRequest request
	) {
		return ResponseEntity.ok()
			.body(tableService.changeEmpty(orderTableId, request))
			;
	}

	@PutMapping("/{orderTableId}/number-of-guests")
	public ResponseEntity<TableResponse> changeNumberOfGuests(
		@PathVariable final Long orderTableId,
		@RequestBody final TableGuestsUpdateRequest request
	) {
		return ResponseEntity.ok()
			.body(tableService.changeNumberOfGuests(orderTableId, request))
			;
	}
}
