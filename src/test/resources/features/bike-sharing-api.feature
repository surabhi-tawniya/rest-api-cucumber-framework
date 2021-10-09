Feature: City Bike API
"""
  As a biker I would like to know the exact location of city bikes around the world with city bike api.
  """

   Background:
   Given I access the bike sharing api url

  Scenario: As a biker I want to verify the status code and content type of city bike apis response

  Scenario: As a biker I want to verify the status code and content type of city bike apis response
    When I provide "/v2/networks" as a resource
    And I request list of networks
    Then I verify the response code as 200
    And I verify the content type as json

  Scenario: As a biker I want to verify the size of networks array should be more than 0 in response
    When I provide "/v2/networks" as a resource
    And I request list of networks
    Then I verify the response code as 200
    And I verify the size of networks is greater than 0

  Scenario Outline: As a biker I want to verify if the <city> city is in <country> country and their corresponding latitude and longitude
    When I provide "/v2/networks" as a resource
    And I request list of networks
    Then I verify the response code as 200
    And I verify the city "<city>" is in country "<country>"
    And I verify the latitude <latitude> and longitude <longitude>
    Examples:
      | city      | country | latitude | longitude |
      | Frankfurt | DE      | 50.1072  | 8.66375   |
      | Moscow    | RU      | 55.75    | 37.616667 |

  Scenario: As a biker I want to verify the field filtering in response
    When I pass the filter fields in resource url
      | id   |
      | name |
      | href |
    And I provide "/v2/networks/visa-frankfurt" as a resource
    And I request list of networks
    Then I verify the response code as 200
    And I verify the response is rendered only with filter fields
      | id   |
      | name |
      | href |

  Scenario: As a biker I want to verify if not found status in case of invalid network id
    When I provide "/v2/networks-invalid-url" as a resource
    And I request list of networks
    Then I verify the response code as 404

#  Scenario Outline: Verify response code after sending <http_method> http method
#    When I enter "/v2/networks" as a resource
#    When I send the "<http_method>" http method
#    Then I verify the response code as 404
#
#    Examples:
#      | http_method |
#      | post        |
#      | put         |
#      | delete      |