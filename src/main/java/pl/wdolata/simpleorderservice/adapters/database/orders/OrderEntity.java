package pl.wdolata.simpleorderservice.adapters.database.orders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pl.wdolata.simpleorderservice.adapters.database.orders.model.OrderItemsData;

@Table(name = "orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("coupon_code")
    private String couponCode;

    private String currency;

    private BigDecimal subtotal;

    private BigDecimal total;

    @Column("items")
    private OrderItemsData items;

    @Column("created_at")
    private Instant createdAt;

    // The id is assigned by the application, so Spring Data cannot tell an insert from an update by itself.
    @Override
    @Transient
    public boolean isNew() {
        return true;
    }
}
