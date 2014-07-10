var dashboardApp = angular.module('dashboardApp', ['ngResource']);

dashboardApp.controller('DashboardCtrl', function($scope, $http, $timeout){
	
	$scope.hosturl = 'http://localhost:8080/data-stream-cache/cache/streaming-data';
//	$scope.hosturl = 'http://cache-kunal.rhcloud.com/data-stream-cache/cache/streaming-data';
	$scope.keys = [];
	$scope.tweets = [];

	$scope.searchText = "";
    $scope.itemsSelectedArr = [];
    $scope.itemsArr = [];
    $scope.itemsDisplaPanel = false;
    
    $scope.searchMe = function (search) {
        $scope.itemsArr = [];
        $scope.itemsDisplaPanel = false;
        if (search.length > 2) {
            var foundText = containsText(search);
            $scope.itemsDisplaPanel = (foundText) ? true : false;
        }
    };

    var n = ["Action Comics", "Detective Comics", "Superman", "Fantastic Four", "Amazing Spider-Man", "Batman Series", "Repoman Seeks", "Love Comics", "Anime Comics","Test Only"];
    var containsText = function (search) {
        var gotText = false;
        for (var i in $scope.tweets) {
//        for (var i in n) {
            var re = new RegExp(search, "ig");
            var s = re.test($scope.tweets[i]);
//            var s = re.test(n[i]);
            if (s) {
                $scope.itemsArr.push($scope.tweets[i]);
//                $scope.itemsArr.push(n[i]);
                gotText = true;
            }
        }
        return gotText;
    };

	$scope.getKeys = function(){
		$http.get($scope.hosturl + '/keys')
			.success(function(data){
				$scope.keys = data;
				$scope.getTweets($scope.keys[0]);
				//$timeout(getKeys, 4000);
			});
	}
	
	$scope.getTweets = function(key){
		$http.get($scope.hosturl + '/key/' + key)
		.success(function(data){
			$scope.tweets = data.slice().reverse();
//			$timeout(getTweets, 10000);
		})
		.error(function(data, status){
			$scope.tweets = 'Big error! ' + status;
//			$timeout(getTweets, 10000);
		});
	}
	
	$scope.getKeys();
	$scope.getTweets($scope.keys[0]);

});

dashboardApp.directive('ngsearchtext', function () {
    return function (scope, element, attrs) {
        element.bind("keyup", function (event) {
            if (event.which !== 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.ngsearchtext);
                });

                event.preventDefault();
            }
        });
    };
});