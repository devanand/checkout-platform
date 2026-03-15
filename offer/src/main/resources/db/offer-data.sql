INSERT INTO offers (id, type, description, valid_from, valid_until, required_quantity, bundle_price, percentage)
VALUES (1, 'MULTI_BUY', 'Buy 2 apples for 0.45', DATE '2026-03-01', DATE '2026-03-31', 2, 0.45, NULL);

INSERT INTO offers (id, type, description, valid_from, valid_until, required_quantity, bundle_price, percentage)
VALUES (2, 'PERCENT_DISCOUNT', '10% discount on bananas', DATE '2026-03-01', DATE '2026-03-31', NULL, NULL, 10);

INSERT INTO offer_assignments (id, product_id, offer_id, priority)
VALUES (1, 'APPLE', 1, 1);

INSERT INTO offer_assignments (id, product_id, offer_id, priority)
VALUES (2, 'BANANA', 2, 1);