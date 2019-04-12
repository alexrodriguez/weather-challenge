<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<%--    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.1/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr"
          crossorigin="anonymous">--%>
    <link href="https://fonts.googleapis.com/css?family=Lato" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value="/css/jquery.typeahead.min.css"/>">
    <title>Weather Challenge</title>
</head>
<body>
<div class="container">
    <!-- Content here -->
    <div class="row justify-content-center">
        <div id="main" class="col-10">
            <form:form method="get" modelAttribute="search" cssClass="needsvalidation" novalidate="novalidate">
                <div class="typeahead__container">
                    <label for="cities" class="lead"><spring:message code="search.input.label"/> </label>
                    <div class="typeahead__field">
                        <div class="typeahead__query">
                            <form:input path="cities" type="search" cssClass="js-typeahead" autocomplete="off"/>
                        </div>
                        <div class="typeahead__button">
                            <button type="submit">
                                <span class="typeahead__search-icon"></span>
                            </button>
                        </div>
                        <div class="invalid-feedback">
                            <form:errors path="cities"/>
                        </div>
                    </div>
                </div>
            </form:form>
            <div id="error-message" class="alert alert-danger mt-3 d-none" role="alert">
                <h4 class="alert-heading">Oops!</h4>
                <p>We are having problems fulfilling your request. Please come back later</p>
            </div>
            <c:if test="${not empty reports}">
                <table class="table table-striped mt-3">
                    <thead>
                    <tr>
                        <th scope="col">City</th>
                        <th scope="col">Temperature</th>
                        <th scope="col">Humidity</th>
                    </tr>
                    </thead>
                    <tbody id="weather-reports">
                    <c:forEach items="${reports}" var="weather">
                        <tr>
                            <td>${weather.name}, ${weather.country}</td>
                            <td>${weather.temperature} <span>&#8457;</span></td>
                            <td>${weather.humidity}%</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>
</div>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha384-tsQFqpEReu7ZLhBV2VZlAu7zcOV+rXbYlF2cqB8txI/8aZajjp4Bqd+V6D5IgvKT" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
<!-- Required JavaScript -->
<script src="<c:url value="/js/jquery.typeahead.min.js"/>"></script>
<script src="<c:url value="/js/mustache-3.0.1.js"/>"></script>
<script>
    function getUrlParameter(name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        var results = regex.exec(location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }

    $(function () {
        $('#cities').val('');
    });

    function displayWeatherReport(items) {
        var $main = $('#main');
        $.getJSON('api/v1/current-weather', {
            ids: items.map(function (city) {
                return city.id
            }).join(',')
        }).done(function (data) {
            var html = Mustache.render($('#table-template').html(), {reports: data});
            if ($main.find('table').length) {
                $main.find('table').replaceWith(html);
            } else {
                $(html).appendTo($main);
            }
        }).fail(function (xhr, textStatus, error) {
            $('#error-message').removeClass('d-none');
        });
    }

    typeof $.typeahead === 'function' && $.typeahead({
        input: '.js-typeahead',
        minLength: 3,
        maxItem: 10,
        maxItemPerGroup: 10,
        order: "asc",
        // hint: true,
        searchOnFocus: true,
        blurOnTab: false,
        multiselect: {
            limit: 4,
            limitTemplate: 'You can\'t select more than 4 cities',
            matchOn: ["id"],
            cancelOnBackspace: true,
            data: function() {
                var cities = getUrlParameter('cities'), deferred;
                if (cities !== '') {
                    return $.get("api/v1/cities", {q: getUrlParameter('cities')});
                } else {
                    deferred = $.Deferred();
                    deferred.resolve([]);
                    return deferred;
                }
            },
            callback: {
                onClick: function (node, item, event) {
                    console.log(item);
                    alert(item.name + ' Clicked!');
                },
                onCancel: function (node, item, event) {
                    console.log(item);
                    if (this.items && this.items.length > 0) {
                        $('table').addClass("text-muted")
                    } else {
                        $('table').remove();
                    }
                }
            }
        },
        template: "{{name}}, {{country}}",
        templateValue: "{{name}}, {{country}}",
        display: ["name"],
        emptyTemplate: 'no result for {{query}}',
        dynamic: true,
        source: {
            cities: {
                ajax: {
                    url: 'api/v1/cities',
                    data: {
                        q: '{{query}}'
                    },
                    beforeSend: function () {
                        $('#error-message').addClass('d-none');
                    }
                }
            }
        },
        callback: {
            onClick: function (/*node, a, item, event*/) {
                $('table').addClass('text-muted');
            },
            onSubmit: function (node, form, items, event) {
                event.preventDefault();
                if (items && items.length > 0) {
                    displayWeatherReport(items);
                }
            }
        },
        debug: true
    });
</script>
<script id="table-template" type="text/x-mustache-template">
    <table class="table table-striped mt-3">
        <thead>
        <tr>
            <th scope="col">City</th>
            <th scope="col">Temperature</th>
            <th scope="col">Humidity</th>
        </tr>
        </thead>
        <tbody>
        {{#reports}}
            <tr>
                <td>{{name}}, {{country}}</td>
                <td>{{temperature}} <span>&#8457;</span></td>
                <td>{{humidity}}%</td>
            </tr>
        {{/reports}}
        </tbody>
    </table>
</script>
</body>
</html>
