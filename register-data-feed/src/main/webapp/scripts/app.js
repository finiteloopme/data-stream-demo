'use strict';

angular.module('registerdatafeed',['ngRoute','ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/',{templateUrl:'views/landing.html',controller:'LandingPageController'})
//      .when('/',{templateUrl:'views/User/search.html',controller:'SearchUserController'})
      .when('/Users',{templateUrl:'views/User/search.html',controller:'SearchUserController'})
      .when('/Users/new',{templateUrl:'views/User/detail.html',controller:'NewUserController'})
      .when('/Users/edit/:UserId',{templateUrl:'views/User/detail.html',controller:'EditUserController'})
      .when('/Locations',{templateUrl:'views/Location/search.html',controller:'SearchLocationController'})
      .when('/Locations/new',{templateUrl:'views/Location/detail.html',controller:'NewLocationController'})
      .when('/Locations/edit/:LocationId',{templateUrl:'views/Location/detail.html',controller:'EditLocationController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('LandingPageController', function LandingPageController() {
  })
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
