var bancaWebApp= angular.module('bancaWebApp', ['ngRoute','bancaWebController','bancaWebServices']);
bancaWebApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : '/serverBanca/bancaWeb/login.html',
		controller : 'loginCtrl'
	}).when('/login', {
		templateUrl : '/serverBanca/bancaWeb/login.html',
		controller : 'loginCtrl'
	}).when('/main', {
		templateUrl : '/serverBanca/bancaWeb/main.html',
		controller : 'mainCtrl'
	}).when('/cpwd', {
		templateUrl : '/serverBanca/bancaWeb/cpwd.html',
		controller : 'passCtrl'
	}).when('/cmail', {
		templateUrl : '/serverBanca/bancaWeb/cmail.html',
		controller : 'mailCtrl'
	}).when('/dmovil', {
		templateUrl : '/serverBanca/bancaWeb/dmovil.html',
		controller : 'desmovCtrl'
	}).when('/rgusr', {
		templateUrl : '/serverBanca/bancaWeb/rgusr.html',
		controller : 'regCtrl'
	})
	.when('/logout', {
		templateUrl : '/serverBanca/bancaWeb/main.html',
		controller : 'logOutCtrl'
	}).when('/cuentas/:tipo', {
		templateUrl : '/serverBanca/bancaWeb/cuentas.html',
		controller : 'cuentasCtrl',
		reloadOnSearch: false
	}).when('/transferencia', {
		templateUrl : '/serverBanca/bancaWeb/transferencia.html',
		controller : 'transCtrl'
	}).when('/validateR', {
		templateUrl : '/serverBanca/bancaWeb/main.html',
		controller : 'validateRCtrl'
	}).when('/verTransferencias', {
		templateUrl : '/serverBanca/bancaWeb/lstTrans.html',
		controller : 'lstTransCtrl'
	}).when('/validateT', {
		templateUrl : '/serverBanca/bancaWeb/main.html',
		controller : 'validateTCtrl'
	});
} ]);

