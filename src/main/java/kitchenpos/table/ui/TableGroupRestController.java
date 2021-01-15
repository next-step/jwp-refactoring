package kitchenpos.table.ui;

import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.service.TableGroupServiceJpa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class TableGroupRestController {
    private final TableGroupServiceJpa tableGroupServiceJpa;

    public TableGroupRestController(TableGroupServiceJpa tableGroupServiceJpa) {
        this.tableGroupServiceJpa = tableGroupServiceJpa;
    }

    @PostMapping(value = "/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest tableGroupRequest) {
        final TableGroupResponse tableGroupResponse = tableGroupServiceJpa.create(tableGroupRequest);
        final URI uri = URI.create("/api/table-groups/" + tableGroupResponse.getId());
        return ResponseEntity.created(uri)
                .body(tableGroupResponse)
                ;
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupServiceJpa.ungroup(tableGroupId);
        return ResponseEntity.noContent()
                .build()
                ;
    }
}
