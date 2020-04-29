Feature: Channel REST endpoint
  Perform operation with channel through REST

  @HappyPath
  Scenario Outline: Subscribe to channel <url>
    Given I remove all channels and save max_id
    Given I set queryParam "url" to "<url>"
    When I submit POST request to channels/subscribe endpoint
    Then I should have channel with "<url>" and title "<title>"

  Examples:
  |                 url                                         |               title               |
  |http://acewatkins.libsyn.com/rss                             |The Ace Watkins Presidential Hour  |
  |https://www.cbc.ca/podcasting/includes/undertheinfluence.xml |Under the Influence from CBC Radio |

