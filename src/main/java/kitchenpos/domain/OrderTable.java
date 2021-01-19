package kitchenpos.domain;

import kitchenpos.exception.BadRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TableGroup tableGroup;
    private Integer numberOfGuests = 0;

    public void update(Integer numberOfGuests) {
        if (numberOfGuests == null) {
            throw new BadRequestException("손님 수는 null로 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return numberOfGuests.equals(0);
    }

    public void group(TableGroup tableGroup) {
        if (tableGroup == null) {
            throw new BadRequestException("Table group은 null로 세팅할 수 없습니다.");
        }
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public boolean isGroupped() {
        return Objects.nonNull(tableGroup);
    }

    public Long getTableGroupId() {
        return (tableGroup == null)?null:tableGroup.getId();
    }
}
