package pl.wdolata.simpleorderservice.fixtures

import java.sql.Timestamp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import pl.wdolata.simpleorderservice.adapters.database.orders.OrderEntity
import pl.wdolata.simpleorderservice.adapters.database.orders.OrderRepository
import pl.wdolata.simpleorderservice.support.JsonUtils

/** What is actually stored in the database, read through plain SQL (not through the app's repository). */
@Component
class OrdersFixture {

    @Autowired
    private JdbcTemplate jdbcTemplate

    @Autowired
    private OrderRepository orderRepository

    OrderEntity save(OrderEntity entity) {
        return orderRepository.save(entity)
    }

    Optional<OrderEntity> findEntity(UUID id) {
        return orderRepository.findById(id)
    }

    List<Map<String, Object>> findAll() {
        return jdbcTemplate.queryForList('''
            SELECT id, user_id AS "userId", coupon_code AS "couponCode", currency, subtotal, total,
                   items::text AS items, created_at AS "createdAt"
            FROM orders
            ORDER BY created_at
        ''').collect { row ->
            [
                id        : row.id,
                userId    : row.userId,
                couponCode: row.couponCode,
                currency  : row.currency,
                subtotal  : row.subtotal,
                total     : row.total,
                items     : JsonUtils.parse(row.items as String),
                createdAt : (row.createdAt as Timestamp).toInstant()
            ]
        }
    }

    long count() {
        return jdbcTemplate.queryForObject('SELECT count(*) FROM orders', Long)
    }

    void flush() {
        jdbcTemplate.execute('TRUNCATE TABLE orders')
    }

}
