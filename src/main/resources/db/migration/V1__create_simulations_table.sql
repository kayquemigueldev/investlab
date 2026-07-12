CREATE TABLE simulations (
                             id BIGINT NOT NULL AUTO_INCREMENT,

                             initial_investment DECIMAL(19, 2) NOT NULL,
                             monthly_contribution DECIMAL(19, 2) NOT NULL,
                             interest_rate_percentage DECIMAL(10, 4) NOT NULL,

                             rate_period VARCHAR(30) NOT NULL,
                             contribution_timing VARCHAR(30) NOT NULL,

                             start_date DATE NOT NULL,
                             end_date DATE NOT NULL,
                             number_of_months INT NOT NULL,

                             final_balance DECIMAL(19, 2) NOT NULL,
                             total_invested DECIMAL(19, 2) NOT NULL,
                             total_interest DECIMAL(19, 2) NOT NULL,
                             profitability_percentage DECIMAL(10, 2) NOT NULL,

                             created_at DATETIME(6) NOT NULL,

                             PRIMARY KEY (id),

                             INDEX idx_simulations_created_at (created_at)
);