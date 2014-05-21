angular.module('registerdatafeed').factory('UserResource', function($resource){
    var resource = $resource('users/users/:UserId',{UserId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});