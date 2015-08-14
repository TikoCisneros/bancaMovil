var bancaWebApp= angular.module('bancaWebApp', ['ngRoute','bancaWebController','bancaWebServices']);
bancaWebApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : '/BancaWM/bancaWeb/login.html',
		controller : 'loginCtrl'
	}).when('/login', {
		templateUrl : '/BancaWM/bancaWeb/login.html',
		controller : 'loginCtrl'
	}).when('/main', {
		templateUrl : '/BancaWM/bancaWeb/main.html',
		controller : 'mainCtrl'
	}).when('/cpwd', {
		templateUrl : '/BancaWM/bancaWeb/cpwd.html',
		controller : 'passCtrl'
	}).when('/cmail', {
		templateUrl : '/BancaWM/bancaWeb/cmail.html',
		controller : 'mailCtrl'
	}).when('/dmovil', {
		templateUrl : '/BancaWM/bancaWeb/dmovil.html',
		controller : 'desmovCtrl'
	}).when('/rgusr', {
		templateUrl : '/BancaWM/bancaWeb/rgusr.html',
		controller : 'regCtrl'
	})
	.when('/logout', {
		templateUrl : '/BancaWM/bancaWeb/main.html',
		controller : 'logOutCtrl'
	}).when('/cuentas/:tipo', {
		templateUrl : '/BancaWM/bancaWeb/cuentas.html',
		controller : 'cuentasCtrl',
		reloadOnSearch: false
	}).when('/transferencia', {
		templateUrl : '/BancaWM/bancaWeb/transferencia.html',
		controller : 'transCtrl'
	}).when('/validateR', {
		templateUrl : '/BancaWM/bancaWeb/main.html',
		controller : 'validateRCtrl'
	}).when('/verTransferencias', {
		templateUrl : '/BancaWM/bancaWeb/lstTrans.html',
		controller : 'lstTransCtrl'
	}).when('/validateT', {
		templateUrl : '/BancaWM/bancaWeb/main.html',
		controller : 'validateTCtrl'
	}).when('/validateM', {
		templateUrl : '/BancaWM/bancaWeb/main.html',
		controller : 'validateMCtrl'
	});
} ]);

