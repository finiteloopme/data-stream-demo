

angular.module('registerdatafeed').controller('EditLocationController', function($scope, $routeParams, $location, LocationResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.location = new LocationResource(self.original);
        };
        var errorCallback = function() {
            $location.path("/Locations");
        };
        LocationResource.get({LocationId:$routeParams.LocationId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.location);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.location.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Locations");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Locations");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.location.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});