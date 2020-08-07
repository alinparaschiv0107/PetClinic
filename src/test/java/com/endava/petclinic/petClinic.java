package com.endava.petclinic;

import com.endava.petclinic.models.Owner;
import com.endava.petclinic.models.Pet;
import com.endava.petclinic.models.Type;
import com.endava.petclinic.util.EnvReader;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class petClinic {

    private final Faker faker = new Faker();

    public String getRandDate() {
        int year = (int) (Math.random() * (2019 - 1999 + 1) + 1999);

        int month = (int) (Math.random() * (12 - 1 + 1) + 1);
        String actualMonth;
        if (month < 10) {
            actualMonth = "0" + month;
        } else {
            actualMonth = String.valueOf(month);
        }

        int day = (int) (Math.random() * (30 - 1 + 1) + 1);
        String actualDay;
        if (day < 10) {
            actualDay = "0" + day;
        } else {
            actualDay = String.valueOf(day);
        }

        return String.valueOf(year) + '/' + actualMonth + '/' + actualDay;
    }

    /**
     * Test the Owner Rest Controller from PetClinic API with GET & POST
     */
    @Test
    public void OwnersTest() {
        Owner owner = new Owner();
        owner.setAddress(faker.address().streetAddress());
        owner.setCity(faker.address().city());
        owner.setFirstName(faker.name().firstName());
        owner.setLastName(faker.name().lastName());
        owner.setTelephone(faker.number().digits(10));

        ValidatableResponse response = given()
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .contentType(ContentType.JSON)
                .body(owner)
                //.log().all()
                .post(EnvReader.getOwnersPath())
                //.prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        Integer id = response.extract().jsonPath().getInt("id");

        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .pathParam("ownerId", id)
                //.log().all()
                .get(EnvReader.getOwnersPath() + "/{ownerId}")
                //.prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);
        Owner actualOwner = getResponse.extract().as(Owner.class);
        assertThat(actualOwner, is(owner));
    }


    /**
     * Test the Pet Rest Controller from PetClinic API with GET & POST
     */
    @Test
    public void PetsTest() {

        // Get my owner as a response and convert it to Owner class
        Integer ownerId = 35;
        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .pathParam("ownerId", ownerId)
                //.log().all()
                .get(EnvReader.getOwnersPath() + "/{ownerId}")
                //.prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Owner actualOwner = getResponse.extract().as(Owner.class);

        // Get my pet type as a respone and convert it to Type class
        Integer typeId = 2;
        ValidatableResponse petTypeResponse = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .pathParam("typeId", typeId)
                //.log().all()
                .get(EnvReader.getTypePath() + "/{typeId}")
                //.prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Type type = petTypeResponse.extract().as(Type.class);

        Pet pet = new Pet();
        pet.setName(faker.funnyName().name());
        pet.setBirthDate(getRandDate());
        pet.setOwner(actualOwner);
        pet.setType(type);

        ValidatableResponse petPostResp = given()
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .contentType(ContentType.JSON)
                .body(pet)
                //.log().all()
                .post(EnvReader.getPetsPath())
                // .prettyPeek()
                .then().statusCode(HttpStatus.SC_CREATED);

        Integer petId = petPostResp.extract().jsonPath().getInt("id");

        ValidatableResponse getPetResp = given().baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPort())
                .basePath(EnvReader.getBasePath())
                .pathParam("petId", petId)
                .get(EnvReader.getPetsPath() + "/{petId}")
                //.prettyPeek()
                .then().statusCode(HttpStatus.SC_OK);

        Pet actualPet = getPetResp.extract().as(Pet.class);
        assertThat(actualPet, is(pet));

    }


}
