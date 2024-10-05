package com.groupfive.Insurancemanagementsystem.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupfive.Insurancemanagementsystem.Model.Broker;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BrokerRepository {
	private final Path brokerFilePath;

	public BrokerRepository(String fileStoragePath) {
		this.brokerFilePath = Paths.get(fileStoragePath, "Broker.json");
	}

	public void saveBroker(Broker broker) throws IOException {

		Path dataDirPath = brokerFilePath.getParent();
		if (!Files.exists(dataDirPath)) {
			Files.createDirectories(dataDirPath);
		}

		JSONArray brokersArray = new JSONArray();
		if (Files.exists(brokerFilePath)) {
			String jsonContent = new String(Files.readAllBytes(brokerFilePath)).trim();
			if (!jsonContent.isEmpty()) {
				brokersArray = new JSONArray(jsonContent);
			}
		}

		JSONObject newBrokerJson = new JSONObject();
		newBrokerJson.put("name", broker.getName());
		newBrokerJson.put("email", broker.getEmail());
		newBrokerJson.put("phone", broker.getPhone());
		newBrokerJson.put("password", broker.getPassword());

		brokersArray.put(newBrokerJson);

		try (FileWriter fileWriter = new FileWriter(brokerFilePath.toFile())) {
			fileWriter.write(brokersArray.toString(4));
		}
	}

	private String brokerToJson(Broker broker) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(broker);
		} catch (IOException e) {
			e.printStackTrace();
			return "{}";
		}
	}

	public JSONArray getAllBrokers() throws IOException {
		if (Files.exists(brokerFilePath)) {
			String jsonContent = new String(Files.readAllBytes(brokerFilePath)).trim();

			if (!jsonContent.startsWith("[")) {
				jsonContent = "[" + jsonContent + "]";
			}

			try {
				return new JSONArray(jsonContent);
			} catch (JSONException e) {
				throw new IOException("Broker data is corrupted or invalid JSON.");
			}
		} else {
			return new JSONArray();
		}
	}

	public boolean isValidBroker(String email, String password) throws IOException {
		JSONArray brokersArray = getAllBrokers();
		for (int i = 0; i < brokersArray.length(); i++) {
			JSONObject broker = brokersArray.getJSONObject(i);
			if (broker.getString("email").equals(email) && broker.getString("password").equals(password)) {
				return true;
			}
		}
		return false;
	}
}
