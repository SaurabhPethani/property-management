package com.manage.property;

import com.manage.property.models.Location;
import com.manage.property.models.Property;
import com.manage.property.models.ShortlistedProperty;
import com.manage.property.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PropertyApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Test
	public void testUserEntityPersistence() {
		User user = new User();
		user.setName("John Doe");
		user.setEmail("john.doe@example.com");
		user.setPhoneNumber("1234567890");

		entityManager.persist(user);
		entityManager.flush();

		User persistedUser = entityManager.find(User.class, user.getUserId());
		assertThat(persistedUser).isNotNull();
		assertThat(persistedUser.getName()).isEqualTo("John Doe");
	}

	@Test
	public void testLocationWithCoordinatesPersistence() {
		Location location = new Location();
		location.setCity("San Francisco");
		location.setState("California");
		location.setCountry("USA");
		location.setZipCode("94101");
		location.setLatitude(37.7749);
		location.setLongitude(-122.4194);

		entityManager.persist(location);
		entityManager.flush();

		Location persistedLocation = entityManager.find(Location.class, location.getLocationId());
		assertThat(persistedLocation).isNotNull();
		assertThat(persistedLocation.getCity()).isEqualTo("San Francisco");
		assertThat(persistedLocation.getLatitude()).isEqualTo(37.7749);
		assertThat(persistedLocation.getLongitude()).isEqualTo(-122.4194);
	}

	@Test
	public void testPropertyEntityPersistence() {
		User user = new User();
		user.setName("Jane Smith");
		user.setEmail("jane.smith@example.com");
		user.setPhoneNumber("9876543210");
		entityManager.persist(user);

		Location location = new Location();
		location.setCity("New York");
		location.setState("New York");
		location.setCountry("USA");
		location.setZipCode("10001");
		entityManager.persist(location);

		Property property = new Property();
		property.setUser(user);
		property.setLocationId(location);
		property.setPrice(500000.0);
		property.setType("Apartment");
		property.setStatus("available");
		entityManager.persist(property);
		entityManager.flush();

		Property persistedProperty = entityManager.find(Property.class, property.getPropertyId());
		assertThat(persistedProperty).isNotNull();
		assertThat(persistedProperty.getPrice()).isEqualTo(500000.0);
	}

	@Test
	public void testShortlistedPropertyEntityPersistence() {
		User user = new User();
		user.setName("Alice Brown");
		user.setEmail("alice.brown@example.com");
		user.setPhoneNumber("1234509876");
		entityManager.persist(user);

		Location location = new Location();
		location.setCity("Los Angeles");
		location.setState("California");
		location.setCountry("USA");
		location.setZipCode("90001");
		entityManager.persist(location);

		Property property = new Property();
		property.setUser(user);
		property.setLocationId(location);
		property.setPrice(750000.0);
		property.setType("House");
		property.setStatus("available");
		entityManager.persist(property);

		ShortlistedProperty shortlistedProperty = new ShortlistedProperty();
		shortlistedProperty.setUser(user);
		shortlistedProperty.setProperty(property);
		entityManager.persist(shortlistedProperty);
		entityManager.flush();

		ShortlistedProperty persistedShortlist = entityManager.find(ShortlistedProperty.class, shortlistedProperty.getId());
		assertThat(persistedShortlist).isNotNull();
		assertThat(persistedShortlist.getUser().getEmail()).isEqualTo("alice.brown@example.com");
	}

	@Test
	public void testFetchPropertiesByUser() {
		User user = new User();
		user.setName("Bob Marley");
		user.setEmail("bob.marley@example.com");
		user.setPhoneNumber("1234560000");
		entityManager.persist(user);

		Location location1 = new Location();
		location1.setCity("Austin");
		location1.setState("Texas");
		location1.setCountry("USA");
		location1.setZipCode("73301");
		entityManager.persist(location1);

		Property property1 = new Property();
		property1.setUser(user);
		property1.setLocationId(location1);
		property1.setPrice(300000.0);
		property1.setType("Apartment");
		property1.setStatus("available");
		entityManager.persist(property1);

		Location location2 = new Location();
		location2.setCity("Dallas");
		location2.setState("Texas");
		location2.setCountry("USA");
		location2.setZipCode("75001");
		entityManager.persist(location2);

		Property property2 = new Property();
		property2.setUser(user);
		property2.setLocationId(location2);
		property2.setPrice(450000.0);
		property2.setType("House");
		property2.setStatus("available");
		entityManager.persist(property2);

		entityManager.flush();

		List<Property> properties = entityManager.createQuery(
						"SELECT p FROM Property p WHERE p.user.userId = :userId", Property.class)
				.setParameter("userId", user.getUserId())
				.getResultList();

		assertThat(properties).hasSize(2);
		assertThat(properties).extracting("price").contains(300000.0, 450000.0);
	}
}

