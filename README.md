## Getting Started
1. Setting up database using provided script (**1)
2. Change database connection config ("src/main/resources/application.properties") (**2)
3. Run command 'mvn clean install' in terminal that on the location of this project
4. Run command 'mvn spring-boot:start'
5. Open 'http://localhost:8080/swagger-ui/index.html#/' for api document

## Integration Test
1. Run command 'mvn test' and wait until it show result

** 1.DATABASE CREATION SCRIPT
CREATE TABLE `tb_books` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `publishedDate` date DEFAULT NULL,
  `createdAt` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=599 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

** 2.DATABSE CONNECTION SETTING
spring.datasource.url= "USE YOUR OWN URL"
spring.datasource.username= "USE YOUR OWN USERNAME" 
spring.datasource.password= "USE YOUR OWN PASSWORD"
