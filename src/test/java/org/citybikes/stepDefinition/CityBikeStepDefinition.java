package org.citybikes.stepDefinition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.citybikes.bean.Networks;
import org.citybikes.bean.Root;
import org.citybikes.utils.ConfigFileReader;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CityBikeStepDefinition  {

  private RequestSpecification request;
  private ValidatableResponse response;
  private Root root;
  private Networks foundNetwork;
  private ConfigFileReader configFileReader;

  public CityBikeStepDefinition(ConfigFileReader configFileReader) {
            this.configFileReader = configFileReader;
  }

  @Given("I access the bike sharing api url")
  public void i_access_the_bike_sharing_api_url() throws IOException {
    System.out.println("******************* - step def -"+ this);
    Properties properties = configFileReader.loadEnvironmentProperties();
    System.out.println("************Entered in loadEnvironmentProperties method*********** ");
    request = given().log().all()
            .baseUri(properties.getProperty("citybike.base.uri"));
  }

  @And("I request list of networks")
  public void i_request_list_of_networks() {
    response = request.get().then().log().all();
  }

  @When("I provide {string} as a resource")
  public void i_enter_as_a_resource(String string) {
    request.basePath(string);
  }

  @Then("I verify the response code as {int}")
  public void i_verify_the_response_code_as(Integer statusCode) {
    response.statusCode(statusCode);
    if (! Integer.valueOf(404).equals(statusCode)) {
      root = response.extract().as(Root.class);
    }
  }

  @Then("I verify the content type as json")
  public void i_verify_the_content_type_as_json() {
    response.header("Content-Type", ContentType.JSON.toString());
  }

  @Then("I verify the size of networks is greater than {int}")
  public void i_verify_the_size_of_networks_is_greater_than(Integer int1) {
    response.assertThat().body("networks.size()", greaterThan(0));
  }

  @Then("I verify the city {string} is in country {string}")
  public void i_verify_the_city_is_in_country(String string, String string2) {
    foundNetwork = root.getNetworks().stream()
        .filter(network -> string.equals(network.getLocation().getCity()))
        .findFirst()
        .orElse(null);
    assertThat(foundNetwork, is(not(nullValue())));
    assertThat(foundNetwork.getLocation().getCity(), is(string));
    assertThat(foundNetwork.getLocation().getCountry(), is(string2));
  }

  @Then("I verify the latitude {double} and longitude {double}")
  public void i_verify_the_latitude_and_longitude(Double lang, Double lat) {

  }

  @When("I pass the filter fields in resource url")
  public void i_pass_the_filter_fields_in_resource_url(DataTable dataTable) {
    List<String> filteringFields = dataTable.asList();
    String result = "";

    String filterFields = filteringFields.stream().collect(Collectors.joining(","));
    request.queryParam("fields", filterFields);
    request.log().all();

    response = request.get().then();
  }

  @Then("I verify the response is rendered only with filter fields")
  public void i_verify_the_response_is_render_only_with_filter_fields(DataTable dataTable) {
    List<String> filteringFields = dataTable.asList();

    filteringFields.stream().forEach(field -> response.body("network", hasKey(field)));
  }
/*  @Then("I verify the json schema of the response")
  public void i_verify_the_json_schema_of_the_response() {
    response.body(JsonSchemaValidator
        .matchesJsonSchemaInClasspath("city-bikes-json-schema.json"));
  }*/

  /*@When("I send the {string} http method")
  public void i_send_the_post_http_method(String string) {
    switch (string) {
      case "post" :
        response = request.post().then();
        break;
      case "put" :
        response = request.put().then();
        break;
      case "delete":
        response = request.delete().then();
        break;
    }
  }*/
}
