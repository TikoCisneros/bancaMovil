var bancaWebApp= angular.module('bancaWebApp', ['ngRoute','bancaWebController','bancaWebServices']);
bancaWebApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/login', {
		templateUrl : '/serverBanca/bancaWeb/login.html',
		controller : 'loginCtrl'
	}).when('/main', {
		templateUrl : '/serverBanca/bancaWeb/main.html',
		controller : 'mainCtrl'
	});
} ]);

