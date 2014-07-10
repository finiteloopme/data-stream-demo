angular.module('registerdatafeed').factory('LocationResource', function($resource){
    var resource = $resource('/locations/:LocationId',{LocationId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});