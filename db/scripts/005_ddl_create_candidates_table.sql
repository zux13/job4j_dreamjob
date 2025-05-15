CREATE TABLE candidates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    creation_date DATETIME,
    city_id INT,
    file_id INT,
    FOREIGN KEY (city_id) REFERENCES cities(id),
    FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE SET NULL
);
