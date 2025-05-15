CREATE TABLE vacancies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    creation_date DATETIME,
    visible BOOLEAN NOT NULL,
    city_id INT,
    file_id INT,
    FOREIGN KEY (city_id) REFERENCES cities(id),
    FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE SET NULL
);
