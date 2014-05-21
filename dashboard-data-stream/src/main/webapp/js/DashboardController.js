var dashboardApp = angular.module('dashboardApp', ['ngResource']);

dashboardApp.controller('DashboardCtrl', function ($scope) {
	
  $scope.phones = [
    {'name': 'Nexus S',
     'snippet': 'Fast just got faster with Nexus S.'},
    {'name': 'Motorola XOOM™ with Wi-Fi',
     'snippet': 'The Next, Next Generation tablet.'},
    {'name': 'MOTOROLA XOOM™',
     'snippet': 'The Next, Next Generation tablet.'}
  ];
  
});

dashboardApp.controller('CacheCtrl', function($scope, $http, $timeout){
	
	(function tick(){
		$http.get('http://localhost:8080/data-stream-cache/cache/cache/author/kunal/')
		.success(function(data){
			$scope.tweets = data.slice().reverse();
			$timeout(tick, 1000);
		})
		.error(function(data, status){
			$scope.tweets = 'Big error! ' + status;
			$timeout(tick, 1000);
		});
		
	})();
	
});