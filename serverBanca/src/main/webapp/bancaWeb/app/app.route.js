var bancaWebApp= angular.module('bancaWebApp', ['ngRoute','moduleController','moduleServices']);
bancaWebApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/login', {
		templateUrl : 'index.html',
		controller : 'loginCtrl'
	});
} ]);

