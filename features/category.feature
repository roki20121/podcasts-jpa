Feature: category REST endpoint
  Perform operation with categories through REST

  Scenario: Create new category
    Given I set queryParam "categoryName" to "my_category"
    When I submit POST request to categories endpoint
    Then I should get 201 Status code
    And I should get notnull uri in "Location" header

  Scenario: List all saved categories
    When I submit GET request to categories endpoint
    Then I should get 200 Status code
    And I should get valid JSON in response body of categories

  Scenario: Delete category
    Given I set queryParam "categoryName" to "my_category"
    When I submit POST request to categories endpoint
    When I submit DELETE request to categories endpoint
    Then I should get 200 Status code
    When I submit GET request to categories endpoint
    Then I should not see category with id 1 in the list

