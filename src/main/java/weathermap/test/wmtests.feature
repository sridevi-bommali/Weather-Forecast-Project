Feature: Weather map acceptance tests
    Scenario Outline: Five day weather forecast data for a city
        Given i am on the weather forecast site at http://localhost:3000/
        When i enter city name as <city>
        Then site should load 5 day forecast for the given city
    Examples:
        |city|
        |glasgow|
        |dundee|
        |edinburgh|
        |aberdeen|
        |perth|
        |stirling|
        
    Scenario Outline: Get three hour forecast
	    Given i am on the weather forecast site at http://localhost:3000/
	    When i enter city name as <city>
	    Then site should load 5 day forecast for the given city
	    When i click on first day of weather summary
	    Then i should be able to see three hour forecast
	     When i click on first day of weather summary
	    Then three hour forecast section must be hidden
	Examples:
        |city|
        |glasgow|
        |dundee|
        |edinburgh|
        |aberdeen|
        |perth|
        |stirling|
        
        