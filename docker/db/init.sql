CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE price (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL,
    value NUMERIC(20, 2) NOT NULL,
    init_date DATE NOT NULL,
    end_date DATE,

    CONSTRAINT fk_product
    FOREIGN KEY (product_id)
    REFERENCES product(id)
    ON DELETE CASCADE,
    CONSTRAINT chk_dates
    CHECK (end_date IS NULL OR init_date < end_date)
);