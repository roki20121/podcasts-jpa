Feature: category REST endpoint
  Perform operation with categories through REST

  @HappyPath
  Scenario: Create new category
    Given I set queryParam "categoryName" to "my_category"
    When I submit POST request to categories endpoint
    Then I should get 201 Status code
    And I should get notnull uri in "Location" header

  @HappyPath
  Scenario: List all saved categories
    When I submit GET request to categories endpoint
    Then I should get 200 Status code
    And I should get valid JSON in response body of categories

  @Smoke
  Scenario: List all saved categories, check Status only
    When I submit GET request to categories endpoint
    Then I should get 200 Status code

  @HappyPath
  Scenario: Delete category
    Given I remove all categories and save max_id
    Given I set queryParam "categoryName" to "my_category"
    When I submit POST request to categories endpoint
    When I want to interact with item with id max_id+1
    When I submit DELETE request to categories endpoint
    Then I should get 200 Status code
    When I submit GET request to categories endpoint
    Then I should not see any categories in the list

