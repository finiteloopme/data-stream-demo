
angular.module('registerdatafeed').controller('NewLocationController', function ($scope, $location, locationParser, LocationResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.location = $scope.location || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Locations/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        LocationResource.save($scope.location, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Locations");
    };
});