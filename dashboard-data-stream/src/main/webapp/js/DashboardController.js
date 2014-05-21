var dashboardApp = angular.module('dashboardApp', ['ngResource']);

//dashboardApp.config(['$routeProvider', function($routeProvider) {
//    $routeProvider
//  .when('/Author/:user', {templateUrl:'views/author.html', controller:'AuthorController'})
//  .otherwise({
//    redirectTo: '/'
//  });
//}]);

//dashboardApp.controller('AuthorController', function($scope, $http, $timeout, $routeParams){
//	
//});

dashboardApp.controller('DashboardCtrl', function($scope, $http, $timeout){
	
	$scope.hosturl = 'http://localhost:8080/data-stream-cache/cache/cache';
	$scope.users = [];
	
	$scope.getUsers = function(){
		$http.get($scope.hosturl + '/authors')
			.success(function(data){
				$scope.users = data;
				$scope.getAuthorTweets($scope.users[0]);
				$timeout(getUsers, 1000);
			});
	}
	
	$scope.getSearchTerms = function(){
		$http.get($scope.hosturl + '/search-terms')
			.success(function(data){
				$scope.searchTerms = data;
				$timeout(getSearchTerms, 1000);
			});
	}
	
	$scope.getAuthorTweets = function(author){
		$http.get($scope.hosturl + '/author/' + author)
		.success(function(data){
			$scope.tweets = data.slice().reverse();
			$timeout(getAuthorTweets, 1000);
		})
		.error(function(data, status){
			$scope.tweets = 'Big error! ' + status;
			$timeout(getAuthorTweets, 1000);
		});
	}
	
	$scope.getUsers();	
	$scope.getSearchTerms();

});