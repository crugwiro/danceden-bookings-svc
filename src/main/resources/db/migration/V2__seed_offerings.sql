-- Reference data for local dev/testing. Dates are fixed (not relative to
-- NOW()) so filtering behavior is reproducible run to run; they'll drift out
-- of "past/current/future" relative to today over real time, which is fine
-- for a learning project without a production environment.

INSERT INTO offering (title, type, description, instructor, price, capacity, start_date, end_date, status) VALUES
('Beginner Salsa', 'CLASS', 'Six-week beginner salsa fundamentals.', 'Maria Lopez', 15.00, 20, '2026-07-01', '2026-08-15', 'COMPLETED'),
('Intro to Bachata', 'CLASS', 'Three-week bachata basics for newcomers.', 'Carlos Diaz', 18.00, 15, '2026-08-20', '2026-09-10', 'COMPLETED'),
('Winter Break Special Workshop', 'CLASS', 'One-off workshop, cancelled due to low signups.', 'Maria Lopez', 25.00, 15, '2026-08-01', '2026-08-05', 'CANCELLED'),
('Hip Hop Fundamentals', 'CLASS', 'Ongoing weekly hip hop class, all levels.', 'Jasmine Reed', 20.00, 25, '2026-09-01', '2026-10-06', 'IN_PROGRESS'),
('8-Week Ballet Intensive', 'PROGRAM', 'Structured 8-week ballet technique program.', 'Elena Petrova', 350.00, 10, '2026-09-08', '2026-11-03', 'IN_PROGRESS'),
('Private Tango Taster', 'CLASS', 'Single-session tango taster, very limited seats.', 'Miguel Torres', 40.00, 4, '2026-09-20', '2026-09-20', 'UPCOMING'),
('Advanced Contemporary Program', 'PROGRAM', '10-week advanced contemporary dance program.', 'Aisha Bello', 500.00, 8, '2026-10-01', '2026-12-15', 'UPCOMING'),
('Kids Jazz Class', 'CLASS', 'After-school jazz class for kids 8-12.', 'Sofia Martins', 12.00, 30, '2026-11-01', '2026-12-20', 'UPCOMING');
