# Weather Challenge

Everybody is curious about Weather around the world. For this coding challenge we would like the tester to create a single page which allows a user to enter a city and retrieve and display the current weather info. 
Here are the criteria: 
- Create a web application using Spring MVC and deploy to Tomcat - Create a single jsp page which has a single input field and a submit button - Make use of regular expression to validate that the city names are valid using JavaScript - On submit validate that the input field is not empty using jQuery - On submit do an AJAX post using jQuery to the server  - On the server call this API  https://openweathermap.org/current parse the result and return the information. - Display the weather information on the page - Use log4j and make use of some of the different logging levels - Add a Restful web service to Spring MVC controller so that by passing the city names as url parameter will return the weather information      - Make use of spring internationalization (it could be only for one language, English)  
Bonus: 
- Allow multiple comma separated cities to be shown and returned - Make use of JQuery animations to make the UI more elegant

## Getting Started

The application is a simple Spring MVC application (not Spring Boot). It uses jQuery to improve user experience, but works without javascript enabled (graceful degradation).

### Prerequisites

You'll need Java (tested with 8 & 10) and Maven installed in your system. You will also need to obtain an API key from OpenWeatherMap. Create a file named `cargo.properties` in the project root and write:

```
weather.api.key=<your api key>
```

### Running

The maven project is configure to deploy to a local container using Cargo. To get it going just execute:

```
mvn clean package cargo:run
```

It will download a Tomcat9 container and deploy the app in it. After that visit.

```
http://localhost:8080/weather-challenge/
```

## Built With

* [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [jQuery](https://jquery.com/) - Improve user experience
* [jQuery Typeahead](http://www.runningcoder.org/jquerytypeahead/) - Search widget
* [Bootstrap](https://getbootstrap.com/) - Visual goodness.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Alex Rodriguez** - *Initial work* - [AlexRodriguez](https://github.com/alexrodriguez)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
