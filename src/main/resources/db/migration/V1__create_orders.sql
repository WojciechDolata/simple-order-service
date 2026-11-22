CREATE TABLE orders
(
    id          UUID PRIMARY KEY,
    coupon_code VARCHAR(64),
    currency    VARCHAR(3)     NOT NULL,
    subtotal    NUMERIC(12, 2) NOT NULL,
    total       NUMERIC(12, 2) NOT NULL,
    items       JSONB          NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL
);
