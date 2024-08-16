package com.findmrotue.fmr;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.findmrotue.fmr.constant.CurrentDbServer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class FmrApplication {

	@Autowired
	DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${server.port}")
	private String port;

	@Value("${spring.datasource.url}")
	private String dbValue;

	public static void main(String[] args) {
		SpringApplication.run(FmrApplication.class, args);
	}

	@PostConstruct
	public void initialize() {
		String baseServerFolder = CurrentDbServer.currentConnectedDb(dbValue);

		ClassPathResource dataResourceGeneral = new ClassPathResource(
				baseServerFolder + "/patch.sql");
		ResourceDatabasePopulator populatorGeneral = new ResourceDatabasePopulator(
				dataResourceGeneral);
		populatorGeneral.execute(dataSource);
//        try {
//            importDataFromJson();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

	private void importDataFromJson() throws IOException {
		// Load JSON file from resources folder
		ClassPathResource resource = new ClassPathResource("json/new.json");
		InputStream inputStream = resource.getInputStream();

		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> shareCompanies = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});

		String checkNameQuery = "SELECT COUNT(*) FROM bus_stop WHERE name = ?";
		String getSequence = "SELECT next_val FROM bus_stop_sequence";
		String updateSequence = "Update bus_stop_sequence set next_val = ?";

		String insertQuery = "INSERT INTO bus_stop (id, name, ltd, lng) " +
				"VALUES (?, ?, ?, ?)";

		for (Map<String, Object> company : shareCompanies) {
			String name = company.get("name").toString();

			// Truncate symbol to 10 characters if necessary
			if (name.length() > 15) {
				name = name.substring(0, 15);
			}

			int nameCount = jdbcTemplate.queryForObject(checkNameQuery, new Object[]{"name"}, Integer.class);
			int sequenceCount = jdbcTemplate.queryForObject(getSequence, Integer.class);

			if (nameCount == 0) {
				jdbcTemplate.update(
						insertQuery,
						sequenceCount,
						company.get("name"),
						company.get("lat"),
						company.get("lon")
				);
				jdbcTemplate.update(
						updateSequence,
						sequenceCount+1
				);
			} else {
				System.out.println("Skipping record with name: " + name + " as it already exists in the database.");
			}
		}

		inputStream.close();
	}

}
